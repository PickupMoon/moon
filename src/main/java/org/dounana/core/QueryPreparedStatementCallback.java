package org.dounana.core;

import org.dounana.utils.JdbcUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QueryPreparedStatementCallback<T> implements PreparedStatementCallback<List<T>>{

    private RowMapper<T> rowMapper;
    private Object[] args;

    public QueryPreparedStatementCallback(RowMapper<T> rowMapper, Object[] args) {
        this.rowMapper = rowMapper;
        this.args = args;
    }

    @Override
    public List<T> doInPreparedStatementCallback(PreparedStatement preparedStatement) {

        try {
            JdbcUtil.doSetValues(preparedStatement,args);
            ResultSet resultSet = preparedStatement.executeQuery();
            return new RowResultExtractor<T>(rowMapper).resultConduct(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.closeStatement(preparedStatement);
        }
        return null;
    }


}
