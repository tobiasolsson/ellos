package org.ellos.test.models;

import java.util.List;

public class ResponseObject {
    private List<Product> products;
    private double averagePrice;

    public ResponseObject(List<Product> products, double averagePrice) {
        this.products = products;
        this.averagePrice = averagePrice;
    }
}
