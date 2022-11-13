package com.warehouse.entities;

import java.util.List;

public class Order {
    private int id;
    private String productPositionList;

    public Order(int id, String productPositionList) {
        this.id = id;
        this.productPositionList = productPositionList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProductList(String productPositionList) {
        this.productPositionList = productPositionList;
    }

    public int getId() {
        return id;
    }

    public String getProductList() {
        return productPositionList;
    }

}
