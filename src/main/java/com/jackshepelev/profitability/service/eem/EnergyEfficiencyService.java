package com.jackshepelev.profitability.service.eem;

import com.jackshepelev.profitability.entity.eem.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.eem.EnergyEfficiencyMeasureRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class EnergyEfficiencyService extends AbstractService<EnergyEfficiencyMeasure, EnergyEfficiencyMeasureRepository> {

    @Autowired
    public EnergyEfficiencyService(EnergyEfficiencyMeasureRepository repository, MessageSource messageSource) {
        super(repository, messageSource);
    }

    @Override
    public EnergyEfficiencyMeasure update(EnergyEfficiencyMeasure entity, Locale locale) throws ProfitabilityException {
        return null;
    }
}
