package com.jackshepelev.profitability.service.measure;

import com.jackshepelev.profitability.binding.BindingEEMInputData;
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

        if (data.getEnergyEfficiencies().stream().allMatch(energyEfficiency -> energyEfficiency.getValue().compareTo(BigDecimal.valueOf(0)) == 0)) {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.ee-must-exist", null, locale)
            );
        }

        Project project = projectService.findById(projectID, locale);

        if (project == null){
            throw new ProfitabilityException(
                    messageSource.getMessage("error.project-not-exist", null, locale)
            );
        }
        EnergyEfficiencyMeasure measure = new EnergyEfficiencyMeasure();
        measure.setInputEEMData(new InputEEMData());

        indicatorsEEMService.setInputData(measure, data);

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

        if (data.getEnergyEfficiencies().stream().allMatch(energyEfficiency -> energyEfficiency.getValue().compareTo(BigDecimal.valueOf(0)) == 0)) {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.ee-must-exist", null, locale)
            );
        }

        Optional<EnergyEfficiencyMeasure> optionalMeasure = repository.findById(eemID);

        if (optionalMeasure.isPresent()){
            EnergyEfficiencyMeasure measure = optionalMeasure.get();
            Project project = projectService.findById(measure.getProject().getId(), locale);

            if (project == null){
                throw new ProfitabilityException(
                        messageSource.getMessage("error.project-not-exist", null, locale)
                );
            }

            indicatorsEEMService.setInputData(measure, data);

            measure.getInputEEMData().getEnergyEfficiencies()
                    .forEach(
                            energyEfficiency -> energyEfficiency
                                    .setValue(
                                            data.getEnergyEfficiencies()
                                                    .stream()
                                                    .filter(
                                                            ee -> ee.getEnergyType().getId() == energyEfficiency.getEnergyType().getId()
                                                    ).findFirst()
                                                    .get()
                                                    .getValue()
                                    )
                    );

            indicatorsEEMService.calculateResultData(measure, project);

            return repository.save(measure);
        } else {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.project-not-exist", null, locale)
            );
        }
    }
}
