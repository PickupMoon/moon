package org.dounana.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementCallback<T> {
    T doInPreparedStatementCallback(PreparedStatement preparedStatement) throws SQLException;
}
