package com.jackshepelev.profitability.service.project;

import com.jackshepelev.profitability.entity.project.EnergyType;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.project.EnergyTypeRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EnergyTypeService extends AbstractService<EnergyType, EnergyTypeRepository> {

    @Autowired
    public EnergyTypeService(EnergyTypeRepository repository, MessageSource messageSource) {
        super(repository, messageSource);
    }

    @Override
    public EnergyType update(EnergyType entity, Locale locale) throws ProfitabilityException {
        return null;
    }
}
