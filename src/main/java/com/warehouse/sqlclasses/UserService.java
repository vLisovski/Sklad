package com.warehouse.sqlclasses;

import com.warehouse.entities.Order;
import com.warehouse.entities.ProductPosition;

import java.util.List;

public class UserService {
    private DbManager dbManager;

    public UserService() throws Exception {
        try {
            dbManager = DbManager.getInstance();
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public void insertProduct(String name, int count) throws Exception {
        try {
            dbManager.getSqlMethods().insertProduct(name, count);
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public void updateProduct(String name, int count) throws Exception {
        try {
            dbManager.getSqlMethods().updateProduct(name, count);
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public int selectProductCount(String name) throws Exception {
        try {
            return dbManager.getSqlMethods().selectProductCount(name);
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public void deleteProduct(String name) throws Exception {
        try {
            dbManager.getSqlMethods().deleteProduct(name);
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public List<ProductPosition> selectProductsByName(String name) throws Exception {
        try {
            return dbManager.getSqlMethods().selectProductsByName(name);
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public List<ProductPosition> selectProductsById(int id) throws Exception {
        try {
            return dbManager.getSqlMethods().selectProductsById(id);
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public boolean findProductByName(String name) throws Exception {
        try {
            return dbManager.getSqlMethods().findProductByName(name);
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public List<String> selectTasks() throws Exception {
        try {
            return dbManager.getSqlMethods().selectTasks();
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public List<Order> selectOrders() throws Exception {
        try {
            return dbManager.getSqlMethods().selectOrders();
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }

    public void deleteOrderById(int id) throws Exception {
        try {
            dbManager.getSqlMethods().deleteOrderById(id);
        } catch (Exception e) {
            throw new Exception("Database initialization error: " + e.getMessage());
        }
    }
}
