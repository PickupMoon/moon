package org.dounana.core;

import java.sql.ResultSet;

public interface ResultExtractor<T> {

    T resultConduct(ResultSet resultSet);
}
