package com.jackshepelev.profitability.service;

import com.jackshepelev.profitability.entity.AbstractEntity;
import com.jackshepelev.profitability.exception.ProfitabilityException;
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
    public E findById(long id, Locale locale) throws ProfitabilityException {
        return repository.findById(id)
                .orElseThrow(() -> new ProfitabilityException(
                        messageSource.getMessage("error.user-not-exist", null, locale)
                ));
    }

    public abstract E save(E entity);

    @Override
    public abstract E update(E entity, Locale locale) throws ProfitabilityException;

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
