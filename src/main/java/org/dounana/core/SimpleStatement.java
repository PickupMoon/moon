package org.dounana.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleStatement implements CreateStatement{

    Connection connection;

    public SimpleStatement(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Statement create() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
