package org.dounana.core;

import java.sql.ResultSet;
import java.util.List;

public interface ResultExtractor<T> {

    T resultConduct(ResultSet resultSet);
}
