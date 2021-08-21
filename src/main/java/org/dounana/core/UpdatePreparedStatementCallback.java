package org.dounana.core;

import org.dounana.JdbcException;
import org.dounana.utils.JdbcUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatePreparedStatementCallback implements PreparedStatementCallback<Integer>{

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
             throw new JdbcException("prepareStatement callback error !", e);
         }finally {
             JdbcUtil.closeStatement(preparedStatement);
         }
    }
}
