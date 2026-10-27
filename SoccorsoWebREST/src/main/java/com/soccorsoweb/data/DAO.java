package com.soccorsoweb.data;

import java.util.List;

public interface DAO<T, ID> {

    T findById(ID id);

    List<T> findAll();

    T save(T entity);

    void deleteById(ID id);

    void delete(T entity);
}
