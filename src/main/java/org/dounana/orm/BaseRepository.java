package org.dounana.orm;

import org.dounana.core.JdbcOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BaseRepository<T, ID> implements Repository<T, ID> {

    private JdbcOperation jdbcOperation;

    private Class<T> targetEntityClass;

    SqlMapper<T> sqlMapper;

    Logger logger = LoggerFactory.getLogger(BaseRepository.class);

    public BaseRepository(JdbcOperation jdbcOperation, Class<T> targetEntityClass) {
        this.jdbcOperation = jdbcOperation;
        this.targetEntityClass = targetEntityClass;
        this.sqlMapper = new SqlMapper<>(targetEntityClass);

    }


    public T findById(ID id) {

        logger.info("selectSQL:" + sqlMapper.selectSql);
        T entity = jdbcOperation.queryObject(sqlMapper.selectSql, targetEntityClass, id);
        return entity;
    }

    public List<T> findAll() {

        logger.info("select all entities sql:" + sqlMapper.selectAllSql);
        List<T> entityList = jdbcOperation.queryList(sqlMapper.selectAllSql, targetEntityClass);
        return entityList;
    }

    public int save(T entity) {

        List<String> tableColumns = sqlMapper.allTableColumns;
        Object[] args = new Object[tableColumns.size()];

        for (int i = 0; i < tableColumns.size(); i++) {
            try {
                String propertyName  = sqlMapper.entityTableColumnMap.get(tableColumns.get(i));

                Field declaredField = targetEntityClass.getDeclaredField(propertyName);
                declaredField.setAccessible(true);
                Object argV = declaredField.get(entity);
                args[i] = argV;

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        logger.info("insert into table sql:" + sqlMapper.insertSql);
        return jdbcOperation.executeUpdate(sqlMapper.insertSql,args);
    }

    public boolean update(T entity) {

        List<String> updateColumnsList = sqlMapper.updateTableColumns;
        Object[] args = new Object[updateColumnsList.size()+1];

        for (int i = 0; i < updateColumnsList.size(); i++) {
            try {
                String tableColumnName = updateColumnsList.get(i);
                String propertyName  = sqlMapper.entityTableColumnMap.get(tableColumnName);
                Field declaredField = targetEntityClass.getDeclaredField(propertyName);
                declaredField.setAccessible(true);
                Object argV = declaredField.get(entity);
                args[i] = argV;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // todo
        args[updateColumnsList.size()] = 33;
        logger.info("update  table sql:" + sqlMapper.updateSql);
        Integer integer = jdbcOperation.executeUpdate(sqlMapper.updateSql, args);
        if (integer > 0) {
            return true;
        }
        return false;
    }

    public int delete(ID id) {

        logger.info("delete sql:" + sqlMapper.deleteSql);

        return jdbcOperation.executeUpdate(sqlMapper.deleteSql);
    }
}
