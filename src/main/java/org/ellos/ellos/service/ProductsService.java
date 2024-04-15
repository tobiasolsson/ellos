package org.ellos.ellos.service;

import com.google.gson.*;
import org.ellos.ellos.exception.ResourceNotFoundException;
import org.ellos.ellos.models.Product;
import org.ellos.ellos.models.ProductReport;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class ProductsService {
    private static final String BASE_URL = "https://www.ellos.se/api/articles";

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
        } catch (IOException e) {
            throw new RuntimeException("Error reading from URL: " + link);
        }

        return finalJson;
    }

    private ProductReport getProductsFromJson(JsonElement json, String filter) {
        List<Integer> prices = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        boolean filterNullOrEmpty = filter == null || filter.isEmpty();

        try {
            // get the pagination
            int last = json.getAsJsonObject().get("pagination").getAsJsonObject().get("last").getAsInt();

            for (int i = 1; i < last; i++) {
                JsonArray articles = json.getAsJsonObject().getAsJsonArray("articles");

                for (JsonElement articleElement : articles) {
                    JsonObject article = articleElement.getAsJsonObject();
                    String name = article.get("name").getAsString();
                    int currentPrice = article.get("currentPrice").getAsInt();

                    if (filterNullOrEmpty || name.toLowerCase().contains(filter.toLowerCase())) {
                        // Loop SKU data:
                        JsonArray skusData = article.getAsJsonArray("skusData");
                        getEachProductSize(skusData, currentPrice, name, prices, products);
                    }
                }

                String nextLink = json.getAsJsonObject().get("links").getAsJsonObject().get("next").getAsString();
                json = JsonParser.parseString(getJsonFromUrl(BASE_URL + nextLink));
            }


        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("Could not parse JSON");
        }

        if (products.isEmpty()) {
            String filterMessage = filterNullOrEmpty ? "" : " with filter: " + filter;
            throw new ResourceNotFoundException("No products found" + filterMessage);
        }

        int sum = prices.stream().mapToInt(Integer::intValue).sum();
        double averagePrice = (double) sum / prices.size();
        averagePrice = BigDecimal.valueOf(averagePrice).setScale(2, RoundingMode.HALF_UP).doubleValue();

        return new ProductReport(products, averagePrice);
    }

    private static void getEachProductSize(JsonArray skusData, int currentPrice, String name, List<Integer> prices,
                                  List<Product> products) {

        for (JsonElement skuDataElement : skusData) {
            JsonObject skuData = skuDataElement.getAsJsonObject();

            // Collect the data
            String sku = skuData.get("sku").getAsString();
            String size = skuData.has("size") ? skuData.get("size").getAsString() : "";

            prices.add(currentPrice);
            products.add(new Product(sku, name, size));
        }
    }

    public ProductReport getProducts(String path, String filter) {
        String json = getJsonFromUrl(BASE_URL + "?path=" + path);
        return getProductsFromJson(JsonParser.parseString(json), filter);
    }
}
