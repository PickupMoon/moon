package org.dounana.core;

import org.dounana.JdbcException;
import org.dounana.orm.BaseRepository;
import org.dounana.utils.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class JdbcTemplate implements JdbcOperation{

    private final Connection connection;
    Logger logger = LoggerFactory.getLogger(BaseRepository.class);

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
            throw new JdbcException("Too many rows !");
        }
        return resultList.get(0);
    }

    @Override
    public Integer executeUpdate(String sql) {
        return execute(new UpdateStatementCallback(sql));
    }

    @Override
    public Integer executeUpdate(String sql, Object... args) {
        return execute(sql,new UpdatePreparedStatementCallback(args));
    }

    @Override
    public void execute(String sql) {
        execute(new UpdateStatementCallback(sql));
    }

    @Override
    public int[] executeBatchUpdate(String sql, List<Object[]> argsList) {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            if (databaseMetaData.supportsBatchUpdates()) {
                for (Object[] objects : argsList) {
                    JdbcUtil.doSetValues(prepareStatement, objects);
                    prepareStatement.addBatch();
                }
                return prepareStatement.executeBatch();
            } else {
                int[] updateRows = new int[argsList.size()];
                for (int i = 0; i < argsList.size(); i++) {
                    Integer integer = execute(sql, new UpdatePreparedStatementCallback(argsList.get(i)));
                    updateRows[i]=integer;
                }
                return updateRows;
            }
        } catch (SQLException e) {
            logger.error("executeBatchUpdate error, sql: {},argsList: {} ",sql, argsList, e);
            throw new JdbcException(e);
        }
    }

    private <T> T execute( StatementCallback<T> statementCallback) {
        try {
            return statementCallback.doStatement(connection.createStatement());
        } catch (SQLException e) {
            logger.error("create statement error ! ",e);
            throw new JdbcException(e);
        }
    }

    private <T> T execute(String sql,PreparedStatementCallback<T> preparedStatementCallback) {
        try {
            return preparedStatementCallback.doInPreparedStatementCallback(connection.prepareStatement(sql));
        } catch (SQLException e) {
            logger.error("create prepareStatement error !",e);
            throw new JdbcException(e);
        }
    }
}
