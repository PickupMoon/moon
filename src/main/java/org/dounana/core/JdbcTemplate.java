package org.dounana.core;

import org.dounana.utils.JdbcUtil;

import java.sql.*;
import java.util.List;

public class JdbcTemplate implements JdbcOperation{

    private Connection connection;

    public JdbcTemplate(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> List<T> queryList(String sql, RowMapper<T> rowMapper) {
        return execute(new QueryStatementCallback<>(sql, rowMapper));
    }

    @Override
    public <T> List<T> queryList(String sql, RowMapper<T> rowMapper, Object... args) {
        return execute(sql,new QueryPreparedStatementCallback<>(rowMapper,args));
    }

    @Override
    public <T> List<T> queryList(String sql, Class<T> targetClass) {
        return queryList(sql,new BeanRowMapper<>(targetClass));
    }

    @Override
    public <T> T queryObject(String sql, RowMapper<T> rowMapper) {
        List<T> mapList = queryList(sql, rowMapper);
        return checkResult(mapList);
    }

    @Override
    public <T> T queryObject(String sql, RowMapper<T> rowMapper,Object... args) {
        List<T> resultList = queryList(sql, rowMapper, args);
        return checkResult(resultList);
    }

    @Override
    public <T> T queryObject(String sql, Class<T> targetClass) {
        List<T> resultList = queryList(sql, targetClass);
        return checkResult(resultList);
    }

    public <T> T queryObject(String sql, Class<T> targetClass, Object... args) {
        List<T> resultList = queryList(sql, new BeanRowMapper<>(targetClass), args);
        return checkResult(resultList);
    }

    private <T> T checkResult(List<T> resultList) {
        if (resultList == null) {
            return null;
        } else if (resultList.size() != 1) {
            throw new RuntimeException("Too many rows");
        }
        return resultList.get(0);
    }

    @Override
    public Integer executeUpdate(String sql) {
        return execute(new UpdateStatementCallback<Integer>(sql));
    }

    @Override
    public Integer executeUpdate(String sql, Object... args) {
        return execute(sql,new UpdatePreparedStatementCallback<Integer>(args));
    }

    @Override
    public void execute(String sql) {
        execute(new UpdateStatementCallback<Integer>(sql));
    }

    @Override
    public int[] executeBatchUpdate(String sql, List<Object[]> argsList) {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            if (databaseMetaData.supportsBatchUpdates()) {
                for (int i = 0; i < argsList.size(); i++) {
                    JdbcUtil.doSetValues(prepareStatement, argsList.get(i));
                    prepareStatement.addBatch();
                }
                return prepareStatement.executeBatch();
            } else {
                int[] updateRows = new int[argsList.size()];
                for (int i = 0; i < argsList.size(); i++) {
                    Integer integer = execute(sql, new UpdatePreparedStatementCallback<Integer>(argsList.get(i)));
                    updateRows[i]=integer;
                }
                return updateRows;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new int[0];
    }

    private <T> T execute( StatementCallback<T> statementCallback) {
        try {
            Connection connection = JdbcUtil.connection();
            return statementCallback.doStatement(connection.createStatement());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T execute(String sql,PreparedStatementCallback<T> preparedStatementCallback) {
        try {
            Connection connection = JdbcUtil.connection();
            return preparedStatementCallback.doInPreparedStatementCallback(connection.prepareStatement(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
