package org.dounana.core;

import org.dounana.utils.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate implements JdbcOperation{

    @Override
    public <T> List<T> queryList(String sql, RowMapper<T> rowMapper) {

        List<T> resultList = new ArrayList<>();
        try {
            Statement statement = JdbcUtil.connection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                resultList.add(rowMapper.rowConvert(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public <T> List<T> queryList(String sql, RowMapper<T> rowMapper, Object[] args) {
        List<T> resultList = new ArrayList<>();
        try {
            PreparedStatement prepareStatement = JdbcUtil.connection().prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                prepareStatement.setObject(i+1,args[i]);
            }
            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next()) {
                resultList.add(rowMapper.rowConvert(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public <T> List<T> queryList(String sql, Class<T> targetClass) {
        return queryList(sql,new BeanRowMapper(targetClass));
    }

    @Override
    public <T> T queryObject(String sql, RowMapper<T> rowMapper) {
        List<T> mapList = queryList(sql, rowMapper);
        if (mapList == null) {
            return null;
        } else if (mapList.size() != 1) {
            throw new RuntimeException("Too many rows !");
        }
        return mapList.get(0);
    }

    @Override
    public <T> T queryObject(String sql, RowMapper<T> rowMapper,Object[] args) {
        List<T> resultList = queryList(sql, rowMapper, args);
        if (resultList == null) {
            return null;
        } else if (resultList.size() != 1) {
            throw new RuntimeException("Too many rows");
        }
        return resultList.get(0);
    }

    @Override
    public <T> T queryObject(String sql, Class<T> targetClass) {
        List<T> resultList = queryList(sql, targetClass);
        if (resultList == null) {
            return null;
        } else if (resultList.size() != 1) {
            throw new RuntimeException("Too many rows");
        }
        return resultList.get(0);
    }

    @Override
    public int executeUpdate(String sql) {
        try {
            Statement statement = JdbcUtil.connection().createStatement();
            int rowCounts = statement.executeUpdate(sql);
            return rowCounts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int executeUpdate(String sql, Object[] args) {
        try {
            PreparedStatement prepareStatement = JdbcUtil.connection().prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                prepareStatement.setObject(i+1,args[i]);
            }
            int rowCounts = prepareStatement.executeUpdate();
            return rowCounts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void execute(String sql) {
        try {
            PreparedStatement prepareStatement = JdbcUtil.connection().prepareStatement(sql);
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
