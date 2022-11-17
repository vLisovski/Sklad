package com.warehouse.sqlclasses;

import com.warehouse.entities.Order;
import com.warehouse.entities.ProductPosition;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLMethods {
    Connection connection = null;

    public SQLMethods(Connection connection) throws SQLException {
        this.connection = connection;
    }

    public boolean findProductByName(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String selectQuery = String.format("SELECT * FROM products WHERE name='%s'", name);
        ResultSet resultSet = statement.executeQuery(selectQuery);
        return resultSet.next();
    }

    public boolean findProductById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        String selectQuery = String.format("SELECT * FROM products WHERE id=%d", id);
        ResultSet resultSet = statement.executeQuery(selectQuery);
        return resultSet.next();
    }

    public void insertProduct(String name, int count) throws SQLException {
        Statement statement = connection.createStatement();
        String insertQuery = String.format("INSERT INTO products (name,count) VALUES ('%s',%d) ", name, count);
        statement.executeUpdate(insertQuery);
        statement.close();
    }

    public void updateProduct(String name, int count) throws SQLException {
        Statement statement = connection.createStatement();
        String updateQuery = String.format("UPDATE products SET count=%d WHERE name = '%s'", count, name);
        statement.executeUpdate(updateQuery);
        statement.close();
    }

    public void deleteProduct(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String updateQuery = String.format("DELETE FROM products WHERE name = '%s' ", name);
        statement.executeUpdate(updateQuery);
        statement.close();
    }

    public int selectProductCount(String name) throws SQLException {
        int count = 0;

        Statement statement = connection.createStatement();
        String selectQuery = String.format("SELECT count FROM products WHERE name='%s'", name);
        ResultSet resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) {
            count = resultSet.getInt("count");
        }

        resultSet.close();
        statement.close();

        return count;
    }

    public List<ProductPosition> selectProductsByName(String name) throws SQLException {
        int id = 0;
        int count = 0;
        String pName = "";
        List<ProductPosition> productPositions = new ArrayList<>();

        Statement statement = connection.createStatement();
        String pfpf = "%" + name + "%";
        String selectQuery = String.format("SELECT * FROM products WHERE name LIKE '%s' ", pfpf );
        ResultSet resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) {
            id = resultSet.getInt("id");
            count = resultSet.getInt("count");
            pName = resultSet.getString("name");
            productPositions.add(new ProductPosition(id, count, pName));
        }
        resultSet.close();
        statement.close();

        return productPositions;
    }

    public List<ProductPosition> selectProductsById(int id) throws SQLException {
        int pId=0;
        int count = 0;
        String pName = "";
        List<ProductPosition> productPositions = new ArrayList<>();

        Statement statement = connection.createStatement();
        String selectQuery = String.format("SELECT * FROM products WHERE id=%d",id);
        ResultSet resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) {
            pId = resultSet.getInt("id");
            count = resultSet.getInt("count");
            pName = resultSet.getString("name");
            productPositions.add(new ProductPosition(pId, count, pName));
        }
        resultSet.close();
        statement.close();

        return productPositions;
    }

    public List<Order> selectOrders() throws SQLException {
        int id = 0;
        String description = "";
        List<Order> orders = new ArrayList<>();

        Statement statement = connection.createStatement();
        String selectQuery = String.format("SELECT * FROM orders");
        ResultSet resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) {

            id = resultSet.getInt("id");
            description = resultSet.getString("description");

            orders.add(new Order(id,description));
        }

        resultSet.close();
        statement.close();

        return orders;
    }

    public void deleteOrderById(int id) throws SQLException {
        Statement statement = connection.createStatement();
        String deleteQuery = String.format("DELETE FROM orders WHERE id=%d",id);
        statement.executeUpdate(deleteQuery);
        statement.close();
    }

    public List<String> selectTasks() throws SQLException {
        List<String> tasks = new ArrayList<>();

        Statement statement = connection.createStatement();
        String selectQuery = String.format("SELECT * FROM tasks");
        ResultSet resultSet = statement.executeQuery(selectQuery);

        while (resultSet.next()) {
            tasks.add(resultSet.getString("description"));
        }

        resultSet.close();
        statement.close();

        return tasks;
    }
}
