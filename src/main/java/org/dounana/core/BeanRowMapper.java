package org.dounana.core;

import java.lang.reflect.Field;
import java.sql.*;

public class BeanRowMapper<T> implements RowMapper<T>{

    private Class<T> targetClass;

    public BeanRowMapper(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public T rowConvert(ResultSet resultSet) {
        try {
            T result = targetClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String label = metaData.getColumnLabel(i);
                int columnType = metaData.getColumnType(i);
                Object columnValue;
                if (columnType == Types.DATE || columnType == Types.TIME) {
                    columnValue = new Date(resultSet.getTimestamp(i).getTime());
                } else {
                    columnValue = resultSet.getObject(i);
                }
                setBeanValue(result, label, columnValue);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setBeanValue(T result, String label, Object labelValue) {
        try {
            Field targetField = result.getClass().getDeclaredField(underScoreToCamelCase(label));
            targetField.setAccessible(true);
            targetField.set(result,labelValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String underScoreToCamelCase(String underScoreStr) {

        char[] labelChars = underScoreStr.toCharArray();
        StringBuilder camelSb = new StringBuilder();

        for (int i = 0; i < labelChars.length; i++) {
            if (labelChars[i] == '_') {
                camelSb.append(Character.toUpperCase(labelChars[++i]));
            } else {
                camelSb.append(labelChars[i]);
            }
        }

        return camelSb.toString();
    }

}
