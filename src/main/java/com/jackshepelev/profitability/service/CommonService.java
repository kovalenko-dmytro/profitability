package com.jackshepelev.profitability.service;

import com.jackshepelev.profitability.entity.AbstractEntity;
import com.jackshepelev.profitability.exception.ProfitabilityException;

import java.util.List;
import java.util.Locale;

public interface CommonService<E extends AbstractEntity> {

    List<E> findAll();
    E findById(long id, Locale locale) throws ProfitabilityException;
    E save(E entity);
    E update(E entity, Locale locale) throws ProfitabilityException;
    void deleteAll();
    void deleteById(long id);
}
