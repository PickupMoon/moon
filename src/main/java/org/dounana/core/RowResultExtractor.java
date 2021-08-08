package org.dounana.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowResultExtractor<T> implements ResultExtractor<List<T>>{

    private RowMapper<T> rowMapper;

    public RowResultExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> resultConduct(ResultSet resultSet) {
        List<T> resultList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                resultList.add(rowMapper.rowConvert(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
