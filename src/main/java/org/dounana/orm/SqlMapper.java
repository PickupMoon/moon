package org.dounana.orm;

import org.dounana.DouTable;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SqlMapper<T> {

    final String selectSql;

    final String selectAllSql;

    final String insertSql;

    final String updateSql;

    final String deleteSql;

    final List<String> allTableColumns;

    final Map<String,String> entityTableColumnMap;

    final List<String> updateTableColumns;

    final String id;



    public SqlMapper(Class<T> entityClass) {
        String tableName = getTableName(entityClass);
        String id = getTableColumnId(entityClass);
        this.id = id;
        this.selectSql = "select * from "+tableName+" where "+id+" = ";
        this.selectAllSql = "select * from "+tableName;
        this.allTableColumns = getTableColumns(entityClass);
        this.entityTableColumnMap = getEntityTableColumns(entityClass);
        this.updateTableColumns = getUpdateTableColumns(entityClass);

        String tableColumns = String.join(",", allTableColumns);
        String updateColumnsPlaceholder = String.join(",", Collections.nCopies(allTableColumns.size(), "?"));
        this.insertSql = "insert into " + tableName + "( "+tableColumns+" ) values ( "+updateColumnsPlaceholder+" )";
        this.updateSql = "update " + tableName + " set "+String.join("= ? ,", updateTableColumns)+ "where "+id+" = ?";
        this.deleteSql = "delete from " + tableName + " where " + id + "= ?";
    }

    private String getTableColumnId(Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class))
                .map(this::getColumnName).findFirst().get();

    }

    private String getColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).name() != null ? field.getAnnotation(Column.class).name() : field.getName();
        } else {
            return field.getName();
        }
    }

    public List<String> getTableColumns(Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(this::getColumnName)
                .collect(Collectors.toList());
    }

    public Map<String,String> getEntityTableColumns(Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .collect(Collectors.toMap(this::getColumnName, Field::getName));
    }

    private String getTableName(Class<T> entityClass) {
        if (entityClass.isAnnotationPresent(DouTable.class)) {
            return entityClass.getAnnotation(DouTable.class).name();
        }
        throw new RuntimeException("Could not get the table !");
    }

    private List<String> getUpdateTableColumns(Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(declaredField -> declaredField.isAnnotationPresent(Column.class) && !declaredField.isAnnotationPresent(Id.class))
                .map(declaredField -> declaredField.getAnnotation(Column.class).name() == null ? declaredField.getName():declaredField.getAnnotation(Column.class).name() )
                .collect(Collectors.toList());
    }
}
