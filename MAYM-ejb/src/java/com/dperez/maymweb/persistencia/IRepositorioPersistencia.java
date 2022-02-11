package com.dperez.maymweb.persistencia;

import java.util.List;

public interface IRepositorioPersistencia<T> {
    public T find(T t);
    public List<T> findAll();
    public void delete(T t);
    public T update(T t);
    public T create(T t);
}
