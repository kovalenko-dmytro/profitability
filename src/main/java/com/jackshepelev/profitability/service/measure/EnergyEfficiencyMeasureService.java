package com.jackshepelev.profitability.service.measure;

import com.jackshepelev.profitability.binding.BindingEEMInputData;
import com.jackshepelev.profitability.binding.ValidList;
import com.jackshepelev.profitability.entity.measure.EnergyEfficiency;
import com.jackshepelev.profitability.entity.measure.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.entity.measure.InputEEMData;
import com.jackshepelev.profitability.entity.measure.ResultEEMData;
import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.measure.EnergyEfficiencyMeasureRepository;
import com.jackshepelev.profitability.repository.measure.EnergyEfficiencyRepository;
import com.jackshepelev.profitability.service.AbstractService;
import com.jackshepelev.profitability.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class EnergyEfficiencyMeasureService
        extends AbstractService<EnergyEfficiencyMeasure, EnergyEfficiencyMeasureRepository> {

    private final ProjectService projectService;
    private final EnergyEfficiencyRepository energyEfficiencyRepository;
    private final IndicatorsEEMService indicatorsEEMService;

    @Autowired
    public EnergyEfficiencyMeasureService(EnergyEfficiencyMeasureRepository repository,
                                          MessageSource messageSource,
                                          ProjectService projectService,
                                          EnergyEfficiencyRepository energyEfficiencyRepository,
                                          IndicatorsEEMService indicatorsEEMService) {

        super(repository, messageSource);
        this.projectService = projectService;
        this.energyEfficiencyRepository = energyEfficiencyRepository;
        this.indicatorsEEMService = indicatorsEEMService;
    }

    public EnergyEfficiencyMeasure save(long projectID,
                                        BindingEEMInputData data,
                                        Locale locale) throws ProfitabilityException {

        validateEnergyEfficiencyValues(data.getEnergyEfficiencies(), locale);

        Project project = projectService.findById(projectID, locale);

        if (project == null){
            throw new ProfitabilityException(
                    messageSource.getMessage("error.project-not-exist", null, locale)
            );
        }

        EnergyEfficiencyMeasure measure = new EnergyEfficiencyMeasure();
        measure.setInputEEMData(new InputEEMData());

        indicatorsEEMService.applyInputData(measure, data);

        List<EnergyEfficiency> energyEfficiencies = data.getEnergyEfficiencies();
        energyEfficiencyRepository.saveAll(energyEfficiencies);

        energyEfficiencies.forEach(energyEfficiency -> energyEfficiency.setMeasure(measure));
        measure.getInputEEMData().setEnergyEfficiencies(energyEfficiencies);
        measure.setResultEEMData(new ResultEEMData());
        indicatorsEEMService.calculateResultData(measure, project);
        measure.setProject(project);

        return repository.save(measure);
    }

    public EnergyEfficiencyMeasure update(long eemID,
                                          BindingEEMInputData data,
                                          Locale locale) throws ProfitabilityException {

        validateEnergyEfficiencyValues(data.getEnergyEfficiencies(), locale);

        Optional<EnergyEfficiencyMeasure> optionalMeasure = repository.findById(eemID);
        if (!optionalMeasure.isPresent()) {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.ee-not-exist", null, locale)
            );
        }

        EnergyEfficiencyMeasure measure = optionalMeasure.get();
        Project project = projectService.findById(measure.getProject().getId(), locale);
        indicatorsEEMService.applyInputData(measure, data);
        updateEnergyEfficiencyValues(measure, data);
        indicatorsEEMService.calculateResultData(measure, project);

        return repository.save(measure);
    }

    private void validateEnergyEfficiencyValues(ValidList<EnergyEfficiency> energyEfficiencies,
                                                Locale locale) throws ProfitabilityException {

        boolean valuesNotExist =
                energyEfficiencies
                        .stream()
                        .allMatch(energyEfficiency ->
                                energyEfficiency.getValue().compareTo(BigDecimal.valueOf(0)) == 0);
        if (valuesNotExist) {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.ee-must-exist", null, locale)
            );
        }
    }

    private void updateEnergyEfficiencyValues(EnergyEfficiencyMeasure measure,
                                              BindingEEMInputData data) {

        List<EnergyEfficiency> storedEnergyEfficiencies = measure.getInputEEMData().getEnergyEfficiencies();
        ValidList<EnergyEfficiency> dataEnergyEfficiencies = data.getEnergyEfficiencies();

        storedEnergyEfficiencies.forEach(energyEfficiency ->
                energyEfficiency.setValue(
                        dataEnergyEfficiencies
                                .stream()
                                .filter(ee ->
                                        ee.getEnergyType().getId() == energyEfficiency.getEnergyType().getId()
                                ).findFirst()
                                .get()
                                .getValue()
                )
        );
    }
}
