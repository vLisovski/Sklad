package com.warehouse.entities;

public class ProductPosition {

    private final int id;
    private final int count;
    private final String name;

    public ProductPosition(int id, int count, String name) {
        this.id = id;
        this.count = count;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
