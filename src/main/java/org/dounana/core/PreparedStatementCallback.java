package org.dounana.core;

import java.sql.PreparedStatement;

public interface PreparedStatementCallback<T> {
    T doInPreparedStatementCallback(PreparedStatement preparedStatement);
}
