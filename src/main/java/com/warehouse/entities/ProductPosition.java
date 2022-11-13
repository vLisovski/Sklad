package com.warehouse.entities;

public class ProductPosition {

    int id;
    int count;
    String name;

    public ProductPosition(int id, int count, String name) {
        this.id = id;
        this.count = count;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setName(String name) {
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
