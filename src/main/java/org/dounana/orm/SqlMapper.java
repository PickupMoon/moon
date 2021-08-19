package org.dounana.orm;

import org.dounana.DouTable;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlMapper<T> {

    private final Class<T> entityClass;

    final String selectSql;

    final String selectAllSql;

    final String insertSql;

    final String updateSql;

    final String deleteSql;

    final List<String> allTableColumns;

    final Map<String,String> entityTableColumnMap;

    final List<String> updateTableColumns;

    final Map<String,String> idMap;



    public SqlMapper(Class<T> entityClass) {
        this.entityClass = entityClass;
        String tableName = getTableName(entityClass);
        String id = getTableColumnId(entityClass);
        Map<String, String> idMap = new HashMap<>();
        this.idMap = idMap;
        this.selectSql = "select * from "+tableName+" where "+id+" = ";
        this.selectAllSql = "select * from "+tableName;
        this.allTableColumns = getTableColumns(entityClass);
        this.entityTableColumnMap = getEntityTableColumns(entityClass);
        this.updateTableColumns = getUpdateTableColumns(entityClass);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < allTableColumns.size(); i++) {
            if (i != allTableColumns.size()-1) {
                stringBuilder.append(allTableColumns.get(i)).append(",");
            } else {
                stringBuilder.append(allTableColumns.get(i)).append(" ");
            }
        }
        this.insertSql = "insert into " + tableName + "( "+stringBuilder+" ) values ( "+generateQuestion(allTableColumns.size())+" )";


        this.updateSql = "update " + tableName + " set "+getUpdatePlaceholder(this.updateTableColumns)+ "where "+id+" = ?";

        this.deleteSql = "delete from " + tableName + " where " + id + "= 33";
    }

    private String generateQuestion(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <n ; i++) {
            if (i == n - 1) {
                stringBuilder.append("?");
            } else {
                stringBuilder.append("? ,");
            }

        }
        return stringBuilder.toString();
    }

    private String getTableColumnId(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                }else {
                    return field.getName();
                }
            }
        }
        throw new RuntimeException("Could not get table Column Id");
    }

    public List<String> getTableColumns(Class<T> entityClass) {

        List<String> allColumns = new ArrayList<>();
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field field : declaredFields) {

            if (field.isAnnotationPresent(Column.class)) {
                allColumns.add(field.getAnnotation(Column.class).name());
            } else {
                allColumns.add(field.getName());
            }
        }
        return allColumns;
    }

    public Map<String,String> getEntityTableColumns(Class<T> entityClass) {
        Map<String,String> entityTableColumnMap = new HashMap<>();
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {

            if (declaredField.isAnnotationPresent(Column.class)) {
                entityTableColumnMap.put(declaredField.getAnnotation(Column.class).name(), declaredField.getName());
            } else {
                entityTableColumnMap.put(declaredField.getName(), declaredField.getName());
            }
        }

        return entityTableColumnMap;
    }

    private String getTableName(Class<T> entityClass) {

        if (entityClass.isAnnotationPresent(DouTable.class)) {
            return entityClass.getAnnotation(DouTable.class).name();
        }
        throw new RuntimeException("Could not get the table !");
    }

    private List<String> getUpdateTableColumns(Class<T> entityClass) {

        List<String> updateTableColumns = new ArrayList<>();
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            if (declaredField.isAnnotationPresent(Column.class)) {
                if (!declaredField.isAnnotationPresent(Id.class)) {
                    updateTableColumns.add(declaredField.getAnnotation(Column.class).name());
                }
            }else {
                updateTableColumns.add(declaredField.getName());
            }
        }
        return updateTableColumns;
    }

    private String getUpdatePlaceholder(List<String> columnsList) {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < columnsList.size(); i++) {

            String updateColumn = columnsList.get(i);
            if (i != columnsList.size() - 1) {
                stringBuilder.append(updateColumn).append(" = ? ,");
            } else {
                stringBuilder.append(updateColumn).append(" = ? ");
            }
        }
        return stringBuilder.toString();
    }

}
