package com.jackshepelev.profitability.service.energytype;

import com.jackshepelev.profitability.entity.project.EnergyType;
import com.jackshepelev.profitability.repository.project.EnergyTypeRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class EnergyTypeService
        extends AbstractService<EnergyType, EnergyTypeRepository> {

    @Autowired
    public EnergyTypeService(EnergyTypeRepository repository, MessageSource messageSource) {
        super(repository, messageSource);
    }
}
