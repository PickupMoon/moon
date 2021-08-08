package org.dounana.core;

import java.sql.ResultSet;

public interface RowMapper<T> {

    T rowConvert(ResultSet resultSet);
}
