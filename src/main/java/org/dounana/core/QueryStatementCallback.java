package org.dounana.core;

import org.dounana.utils.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class QueryStatementCallback<T> implements StatementCallback<List<T>>{

    private String sql;

    private RowMapper<T> rowMapper;

    public QueryStatementCallback(String sql, RowMapper<T> rowMapper) {
        this.sql = sql;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> doStatement(Statement statement) {
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            List<T> resultList = new RowResultExtractor<>(rowMapper).resultConduct(resultSet);
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.closeStatement(statement);
        }
        return null;
    }
}
