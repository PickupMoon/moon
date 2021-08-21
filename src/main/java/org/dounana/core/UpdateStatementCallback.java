package org.dounana.core;

import org.dounana.utils.JdbcUtil;

import java.sql.SQLException;
import java.sql.Statement;

public class UpdateStatementCallback<T> implements StatementCallback<Integer>{

    private final String sql;

    public UpdateStatementCallback(String sql) {
        this.sql = sql;
    }

    @Override
    public Integer doStatement(Statement statement) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.closeStatement(statement);
        }
        return 0;
    }
}
