package com.erp.rest.base;


import lombok.extern.slf4j.Slf4j;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Dependent
public class JPACRUDService<T extends IEntity> implements CRUDService<T> {
    @PersistenceContext(unitName = "JpaPersistence")
    private EntityManager entityManager;

    private Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    private Class<T> getPersistentClass() {
        if (persistentClass == null) {
            persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    private Class<T> resolveEntity(InjectionPoint ip) {
        ParameterizedType type = (ParameterizedType) ip.getType();
        Type[] typeArgs = type.getActualTypeArguments();
        return (Class<T>) typeArgs[0];
    }

    private EntityManager getEntityManager() {
        return entityManager;
    }

    @Inject
    public void inject(InjectionPoint ip) {
        try {
            persistentClass = resolveEntity(ip);
        } catch (Exception e) {
            throw new IllegalArgumentException("Entity class at injection point is invalid", e);
        }
    }

    @Override
    public T create(T entity) {
        try {
            getEntityManager().persist(entity);
        } catch (IllegalArgumentException | PersistenceException | ConstraintViolationException | IllegalStateException e) {
            log.error("cannot create entity" + entity.toString(), e);
        }
        return entity;
    }

    @Override
    public List<T> create(List<T> entities) {
        return entities.stream().peek(this::create).collect(Collectors.toList());
    }

    @Override
    public T get(Object id) {
        T entity;
        try {
            entity = getEntityManager().find(getPersistentClass(), id);
        } catch (IllegalArgumentException | PersistenceException | ConstraintViolationException e) {
            log.error("no entity was found with id " + id, e);
            throw new IllegalStateException();
        }
        if (entity == null) {
            log.error("no entity was found with id " + id);
            throw new IllegalStateException();
        }
        return entity;
    }

    @Override
    public List<T> getAll() {
        String sqlQuery = "SELECT e FROM " + getPersistentClass().getSimpleName() + " e";
        TypedQuery<T> query = getEntityManager().createQuery(sqlQuery, getPersistentClass());
        return query.getResultList();
    }

    @Override
    public T update(T entity) {
        try {
            getEntityManager().merge(entity);
        } catch (IllegalArgumentException | PersistenceException | ConstraintViolationException e) {
            log.error("cannot update entity" + entity.toString(), e);
            throw new IllegalStateException();
        }
        return get(entity.getId());
    }

    @Override
    public void remove(T entity) {
        getEntityManager().remove(get(entity.getId()));
    }

    @Override
    public void removeById(Object id) {
        getEntityManager().remove(get(id));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void tryDo(Consumer<T> consumer, T entity) {
        try {
            consumer.accept(entity);
        } catch (Exception e) {
            log.warn("Exception occurs when calling " + consumer.toString(), e);
        }
    }

    @Override
    public void removeAll() {
        getEntityManager().createQuery("DELETE FROM " + getPersistentClass().getSimpleName() + " e").executeUpdate();
    }
}
