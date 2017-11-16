package com.erp.rest.base;

import java.util.List;
import java.util.function.Consumer;

public interface CRUDService<T> {
    List<T> create(List<T> entities);

    T create(T entity);

    T update(T entity);

    <I> T get(I id);

    List<T> getAll();

    void remove(T entity);

    <I> void removeById(I id);

    void removeAll();

    void tryDo(Consumer<T> consumer, T entity);
}
