package org.dounana.core;

import org.dounana.utils.JdbcUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatePreparedStatementCallback<T> implements PreparedStatementCallback<Integer>{

    private final Object[] args;

    public UpdatePreparedStatementCallback(Object[] args) {
        this.args = args;
    }

    @Override
    public Integer doInPreparedStatementCallback(PreparedStatement preparedStatement) {
         try {
             JdbcUtil.doSetValues(preparedStatement,args);
             return preparedStatement.executeUpdate();
         } catch (SQLException e) {
             e.printStackTrace();
         }finally {
             JdbcUtil.closeStatement(preparedStatement);
         }
         return 0;
    }
}
