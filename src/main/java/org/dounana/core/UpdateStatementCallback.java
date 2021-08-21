package org.dounana.core;

import org.dounana.JdbcException;
import org.dounana.utils.JdbcUtil;

import java.sql.SQLException;
import java.sql.Statement;

public class UpdateStatementCallback implements StatementCallback<Integer>{

    private final String sql;

    public UpdateStatementCallback(String sql) {
        this.sql = sql;
    }

    @Override
    public Integer doStatement(Statement statement) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new JdbcException("statement executeUpdate error !", e);
        }finally {
            JdbcUtil.closeStatement(statement);
        }
    }
}
