package org.dounana.orm;

import org.dounana.core.JdbcOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class BaseRepository<T, ID> implements Repository<T, ID> {

    private final JdbcOperation jdbcOperation;

    private final Class<T> targetEntityClass;

    SqlMapper<T> sqlMapper;

    Logger logger = LoggerFactory.getLogger(BaseRepository.class);

    public BaseRepository(JdbcOperation jdbcOperation, Class<T> targetEntityClass) {
        this.jdbcOperation = jdbcOperation;
        this.targetEntityClass = targetEntityClass;
        this.sqlMapper = new SqlMapper<>(targetEntityClass);
    }

    public T findById(ID id) {
        logger.info("select sql:" + sqlMapper.selectSql);
        return jdbcOperation.queryObject(sqlMapper.selectSql, targetEntityClass, id);
    }

    public List<T> findAll() {
        logger.info("select all sql:" + sqlMapper.selectAllSql);
        return jdbcOperation.queryList(sqlMapper.selectAllSql, targetEntityClass);
    }

    public int save(T entity) {
        List<String> tableColumns = sqlMapper.allTableColumns;
        Object[] args = new Object[tableColumns.size()];
        for (int i = 0; i < tableColumns.size(); i++) {
            String propertyName = sqlMapper.entityTableColumnMap.get(tableColumns.get(i));
            args[i] = getObjectFieldValue(propertyName, entity);
        }
        logger.info("insert sql:" + sqlMapper.insertSql);
        return jdbcOperation.executeUpdate(sqlMapper.insertSql, args);
    }

    public boolean update(T entity) {
        List<String> updateColumnsList = sqlMapper.updateTableColumns;
        Object[] args = new Object[updateColumnsList.size() + 1];
        for (int i = 0; i < updateColumnsList.size(); i++) {
            String tableColumnName = updateColumnsList.get(i);
            String propertyName = sqlMapper.entityTableColumnMap.get(tableColumnName);
            args[i] = getObjectFieldValue(propertyName, entity);
        }
        String propertyName = sqlMapper.entityTableColumnMap.get(sqlMapper.id);
        args[updateColumnsList.size()] = getObjectFieldValue(propertyName, entity);
        logger.info("update sql:" + sqlMapper.updateSql);
        Integer integer = jdbcOperation.executeUpdate(sqlMapper.updateSql, args);
        return integer > 0;
    }

    public int delete(ID id) {
        logger.info("delete sql:" + sqlMapper.deleteSql);
        return jdbcOperation.executeUpdate(sqlMapper.deleteSql, id);
    }

    private Object getObjectFieldValue(String fieldName, Object entity) {
        try {
            Field declaredField = targetEntityClass.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
