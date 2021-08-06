package org.dounana.core;

public interface JdbcOperation {
    <T> T queryList(String sql);
}
