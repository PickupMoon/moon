package org.dounana.core;

import org.dounana.utils.JdbcUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapRowMapper implements RowMapper<Map<String, Object>>{

    @Override
    public Map<String, Object> rowConvert(ResultSet resultSet) {
        Map<String, Object> result = new HashMap<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String label = metaData.getColumnLabel(i);
                result.put(label, resultSet.getObject(label));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.closeResultSet(resultSet);
        }
        return result;
    }
}
