package com.soccorsoweb.data;

import jakarta.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.util.List;


public abstract class BaseDAO<T, ID> implements DAO<T, ID> {

    protected final EntityManager entityManager;
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T findById(ID id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("FROM " + entityClass.getName(), entityClass).getResultList();
    }

    @Override
    public T save(T entity) {
        if (entityManager.contains(entity)) {
            // Se l'entità è già gestita, la merge (aggiorna)
            entity = entityManager.merge(entity);
        } else {
            // Altrimenti, la persiste (inserisce)
            entityManager.persist(entity);
        }
        return entity;
    }

    @Override
    public void deleteById(ID id) {
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public void delete(T entity) {

        if (!entityManager.contains(entity)) {
            entity = entityManager.merge(entity);
        }
        entityManager.remove(entity);
    }
}
