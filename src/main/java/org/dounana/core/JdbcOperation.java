package org.dounana.core;

import java.util.List;

public interface JdbcOperation {

    /**
     * 根据SQL语句查询出相应的结果集
     * @param sql
     * @param rowMapper
     * @param <T>
     * @return
     * 根据输入的Sql查询出相应的结果集：
     * 1、创建数据库的物理连接
     *     1）、根据反射机制加载数据库驱动（Class.forName("相应的数据库驱动包语句")）、引入相应的数据库驱动包。
     *     2)、获取相应的数据库物理连接（DriverManager.getConnection(url,username,password））；
     * 2、根据1 得到的连接对象 获取到 Statement 对象，该对象可以进行Sql语句执行；
     * 3、根据2 得到 sql 执行结果，查询语句的结果集处理类为ResultSet，更新类的语句得到的结果是受影响的行数。
     * 4、根据需求对结果集进行相应处理。
     *     1、或将结果集处理为List<Map<String,Object>>;
     *         涉及将 ResultSet处理为Map<String,Object>
     *     2、或将结果集处理为相应的List<entry>;
     *         涉及将ResultSet处理为相应的entry，需要将数据库的字段与entry的filed对应起来。
     * 5、进行相应的输出。
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

    <T> T queryObject(String sql, Class<T> targetClass, Object... args);


    Integer executeUpdate(String sql);

    Integer executeUpdate(String sql, Object... args);

    void execute(String sql);

    int[] executeBatchUpdate(String sql,List<Object[]> argsList);

}
