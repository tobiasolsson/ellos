package org.ellos.test.service;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.ellos.test.models.Product;
import org.ellos.test.models.ResponseObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class ProductsService {
    private final String BASE_URL = "https://www.ellos.se/api/articles";
    // TODO: ?path=abc... om inte detta finns, ge error (path är en query param, samma med ?filter= sen)
    // TODO: om inget sök finns vad gör jag då, nått error beror på vad api ger?
    // TODO: lägg till filter funktion
    // TODO: avg price, hämta priset endast 1 gång per artikel och inte per storlek
    // TODO: Ska jag verkligen använda pagination? Står 'simple' och 'return products from the articles collection'
    // TODO: Clean up comments

    private String getJsonFromUrl(String link) {
        String finalJson = "";
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output;
                StringBuilder json = new StringBuilder();
                while ((output = bufferedReader.readLine()) != null) {
                    json.append(output);
                }
                finalJson = json.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace(); // TODO: handle exceptions!
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO: Handle this
        }

        return finalJson;
    }

    private ResponseObject getProductsFromJson(JsonElement json) {
        List<Integer> prices = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        JsonElement productsJson = json;
        try {
            // get the pagination
            int last = productsJson.getAsJsonObject().get("pagination").getAsJsonObject().get("last").getAsInt();

            for (int i = 1; i < last; i++) {
                JsonArray articles = productsJson.getAsJsonObject().getAsJsonArray("articles");
                String nextLink = productsJson.getAsJsonObject().get("links").getAsJsonObject().get("next").getAsString();
                for (JsonElement articleElement : articles) {
                    JsonObject article = articleElement.getAsJsonObject();

                    // Loop SKU data:
                    JsonArray skusData = article.getAsJsonArray("skusData");
                    for (JsonElement skuDataElement : skusData) {
                        String sku = "", size = "";
                        JsonObject skuData = skuDataElement.getAsJsonObject();

                        sku = skuData.get("sku").getAsString();
                        size = skuData.has("size") ? skuData.get("size").getAsString() : "";

                        String name = article.get("name").getAsString();
                        int currentPrice = article.get("currentPrice").getAsInt();
                        prices.add(currentPrice);

                        Product product = new Product(sku, name, size);
                        products.add(product);
                    }
                }
                productsJson = JsonParser.parseString(getJsonFromUrl(BASE_URL + nextLink));
            }


        } catch (JsonSyntaxException e) {}

        int sum = prices.stream().mapToInt(price -> price).sum();
        double averagePrice = (double) sum / prices.size();
        double averagePriceRounded = new BigDecimal(averagePrice).setScale(2, RoundingMode.HALF_UP).doubleValue();

        return new ResponseObject(products, averagePriceRounded);
    }

    public ResponseObject getProducts(String path, String filter) {
        ResponseObject products = new ResponseObject(null, 0); // TODO: Gör nått åt detta
        try {
            // Parse the url
            String json = getJsonFromUrl(BASE_URL + "?path=" + path);
            JsonElement element = JsonParser.parseString(json);

            products = getProductsFromJson(element);
        } catch (JsonSyntaxException e) {
            e.printStackTrace(); // TODO: handle
        }


        return products;
    }
}
