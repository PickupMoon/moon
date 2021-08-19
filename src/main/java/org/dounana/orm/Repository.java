package org.dounana.orm;

import java.util.List;

public interface Repository<T, ID> {

    T findById(ID id);

    List<T> findAll();

    int save(T entity);

    boolean update(T entity);

    int delete(ID id);
}
