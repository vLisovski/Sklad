package com.warehouse.sqlclasses;

import java.sql.Connection;
import java.sql.SQLException;

public class DbManager {
    private SQLMethods sqlMethods;

    private DbManager() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        sqlMethods = new SQLMethods(connection);

    }

    public SQLMethods getSqlMethods() {
        return sqlMethods;
    }

    private static DbManager instance;

    public static DbManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }
}
