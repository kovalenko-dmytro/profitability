package com.jackshepelev.profitability.service;

import com.jackshepelev.profitability.entity.AbstractEntity;

import java.util.List;
import java.util.Locale;

public interface CommonService<E extends AbstractEntity> {

    List<E> findAll();
    E findById(long id, Locale locale);
    E save(E entity);
    void deleteById(long id);
}
