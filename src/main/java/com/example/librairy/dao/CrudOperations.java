package com.example.librairy.dao;

import java.util.List;
import java.util.Optional;

public interface CrudOperations<T> {
    Optional<T> findById(int id);
    List<T> findAll();
    T save(T entity);
    T update(T entity);
    void delete(T entity);
    List<T> findByCriteria(List<Criteria> criteriaList);
}
