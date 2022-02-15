package com.dperez.maymweb.persistencia;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RepositorioPersistencia<T> implements IRepositorioPersistencia<T> {

    protected EntityManager entityManager;
    private Class<T> type;

    public RepositorioPersistencia(Class<T> entityClass, EntityManager entityManager) {
        this.type = entityClass;
        this.entityManager = entityManager;
    }

    @Override
    public T find(final Object id) {
        return (T) entityManager.find(type, id);
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> root = criteriaQuery.from(type);
        criteriaQuery.select(root);
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public void delete(final Object objeto) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(objeto);
            entityManager.remove(objeto);
            entityManager.getTransaction().commit();              
        } catch (Exception e) {
             System.out.println("Error al borrar entidad " + type + ": " + e.getLocalizedMessage());
        }
    }

    @Override
    public T update(final T t) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(t);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error al actualizar entidad " + type + ": " + e.getLocalizedMessage());
        }
        return t;
    }

    @Override
    public T create(final T t) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(t);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
             System.out.println("Error al crear entidad " + type + ": " + e.getLocalizedMessage());
        }
        return t;
    }
    
    @Override
    public void finalize() throws Throwable{
        try {
            if(entityManager.isOpen())
                entityManager.close();
        } finally {
            super.finalize();
        }
    }

}
