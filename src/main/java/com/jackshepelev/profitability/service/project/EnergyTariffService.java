package com.jackshepelev.profitability.service.project;

import com.jackshepelev.profitability.entity.project.EnergyTariff;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.project.EnergyTariffRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class EnergyTariffService
        extends AbstractService<EnergyTariff, EnergyTariffRepository> {

    @Autowired
    public EnergyTariffService(EnergyTariffRepository repository, MessageSource messageSource) {
        super(repository, messageSource);
    }

    @Override
    public EnergyTariff update(EnergyTariff entity, Locale locale) throws ProfitabilityException {
        return null;
    }
}
