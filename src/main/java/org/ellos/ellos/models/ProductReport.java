package org.ellos.ellos.models;

import java.util.List;

public class ProductReport {
    private List<Product> products;
    private double averagePrice;

    public ProductReport(List<Product> products, double averagePrice) {
        this.products = products;
        this.averagePrice = averagePrice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getAveragePrice() {
        return averagePrice;
    }
}
