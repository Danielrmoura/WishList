package com.luizalabs.whitelist.business.service;



public interface IService<T> {

 
    T findById(Object id);

    T insert(T objectToSave);

    long update(T objectToSave);

    long delete(Object id);

}
