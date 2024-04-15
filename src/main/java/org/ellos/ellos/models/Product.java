package org.ellos.ellos.models;

public class Product {
    private String sku;
    private String name;
    private String size;

    public Product(String sku, String name, String size) {
        this.sku = sku;
        this.name = name;
        this.size = size;
    }

    public String getSku() {
        return sku;
    }

    public String getSize() {
        return size;
    }

    public String getName() {
        return name;
    }
}
