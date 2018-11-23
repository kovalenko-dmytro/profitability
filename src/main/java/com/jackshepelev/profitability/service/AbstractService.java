package com.jackshepelev.profitability.service;

import com.jackshepelev.profitability.entity.AbstractEntity;
import com.jackshepelev.profitability.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

public abstract class AbstractService<E extends AbstractEntity, R extends CommonRepository<E>>
        implements CommonService<E> {

    protected final R repository;
    protected final MessageSource messageSource;

    @Autowired
    public AbstractService(R repository, MessageSource messageSource) {
        this.repository = repository;
        this.messageSource = messageSource;
    }

    @Override
    public List<E> findAll() {
        return (List<E>) repository.findAll();
    }

    @Override
    public E findById(long id, Locale locale) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public E save(E entity) {
        return  repository.save(entity);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
