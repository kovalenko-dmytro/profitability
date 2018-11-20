package com.jackshepelev.profitability.repository;

import com.jackshepelev.profitability.entity.AbstractEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepository<E extends AbstractEntity>
        extends CrudRepository<E, Long> {
}
