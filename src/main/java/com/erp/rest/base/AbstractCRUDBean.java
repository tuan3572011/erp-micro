package com.erp.rest.base;

import java.util.List;

public abstract class AbstractCRUDBean<T extends IEntity> {
    public abstract CRUDService<T> getCRUDService();

    public T create(T entity) {
        return getCRUDService().create(entity);
    }

    public void tryCreate(T entity) {
        getCRUDService().tryDo(this::create, entity);
    }

    public T get(Long id) {
        return getCRUDService().get(id);
    }

    public List<T> getAll() {
        return getCRUDService().getAll();
    }

    public T update(T entity) {
        return getCRUDService().update(entity);
    }

    public void tryUpdate(T entity) {
        getCRUDService().tryDo(this::update, entity);
    }

    public void remove(Long id) {
        getCRUDService().remove(get(id));
    }

    public void tryRemove(Long id) {
        getCRUDService().tryDo(getCRUDService()::remove, get(id));
    }
}
