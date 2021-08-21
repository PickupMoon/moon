package org.dounana.core;

import org.dounana.utils.JdbcUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class QueryStatementCallback<T> implements StatementCallback<List<T>>{

    private final String sql;
    private final RowMapper<T> rowMapper;

    public QueryStatementCallback(String sql, RowMapper<T> rowMapper) {
        this.sql = sql;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> doStatement(Statement statement) throws SQLException {
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            return new RowResultExtractor<>(rowMapper).resultConduct(resultSet);
        } finally {
            JdbcUtil.closeStatement(statement);
        }
    }
}
