package com.jackshepelev.profitability.service.eem;


import com.jackshepelev.profitability.entity.eem.EnergyEfficiency;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.entity.project.EnergyType;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.eem.EnergyEfficiencyRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EnergyEfficiencyService extends AbstractService<EnergyEfficiency, EnergyEfficiencyRepository> {

    @Autowired
    public EnergyEfficiencyService(EnergyEfficiencyRepository repository, MessageSource messageSource) {
        super(repository, messageSource);
    }

    @Override
    public EnergyEfficiency update(EnergyEfficiency entity, Locale locale) throws ProfitabilityException {
        return null;
    }

    public EnergyEfficiency find(EnergyEfficiencyMeasure measure) {
        return repository.findByEem(measure);
    }

    public EnergyEfficiency find(EnergyEfficiencyMeasure measure, EnergyType type) {
        return repository.findByEemAndAndEnergyType(measure, type);
    }
}
