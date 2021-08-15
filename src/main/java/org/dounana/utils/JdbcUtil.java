package org.dounana.utils;

import java.sql.*;

public abstract class JdbcUtil {

    public static Connection connection() {
        Connection connection = null;
        try {
            //Class.forName("org.sqlite.JDBC");
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/test", "sa","");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void doSetValues(PreparedStatement preparedStatement,Object[] args){
        for (int i = 0; i < args.length; i++) {
            try {
                preparedStatement.setObject(i+1,args[i]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
