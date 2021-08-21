package org.dounana.core;

import java.sql.SQLException;
import java.sql.Statement;

public interface StatementCallback<T> {
     T doStatement(Statement statement) throws SQLException;
}
