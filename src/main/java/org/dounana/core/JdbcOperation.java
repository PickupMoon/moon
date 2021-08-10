package org.dounana.core;

import java.util.List;

public interface JdbcOperation {

    /**
     * 根据SQL语句查询出相应的结果集
     * @param sql
     * @param rowMapper
     * @param <T>
     * @return
     */
    <T> List<T> queryList(String sql, RowMapper<T> rowMapper);

    /**
     * 根据输入的相应的参数，查询具备过滤条件的结果集
     * @param sql
     * @param rowMapper
     * @param args
     * @param <T>
     * @return
     */
    <T> List<T> queryList(String sql, RowMapper<T> rowMapper, Object...args);

    /**
     * 根据SQL查询出相应的javabean类
     * @param sql
     * @param targetClass
     * @param <T>
     * @return
     */
    <T> List<T> queryList(String sql, Class<T> targetClass);

    <T> T queryObject(String sql,RowMapper<T> rowMapper);

    <T> T queryObject(String sql,RowMapper<T> rowMapper, Object... args);

    <T> T queryObject(String sql, Class<T> targetClass);

    Integer executeUpdate(String sql);

    Integer executeUpdate(String sql, Object... args);

    void execute(String sql);

}
