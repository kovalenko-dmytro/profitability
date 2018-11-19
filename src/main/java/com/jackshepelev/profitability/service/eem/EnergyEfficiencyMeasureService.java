package com.jackshepelev.profitability.service.eem;

import com.jackshepelev.profitability.binding.EemInputData;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiency;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.entity.eem.InputEEMData;
import com.jackshepelev.profitability.entity.eem.ResultEEMData;
import com.jackshepelev.profitability.entity.project.EnergyTariff;
import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.eem.EnergyEfficiencyMeasureRepository;
import com.jackshepelev.profitability.repository.eem.EnergyEfficiencyRepository;
import com.jackshepelev.profitability.service.AbstractService;
import com.jackshepelev.profitability.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class EnergyEfficiencyMeasureService extends AbstractService<EnergyEfficiencyMeasure, EnergyEfficiencyMeasureRepository> {

    private final ProjectService projectService;
    private final EnergyEfficiencyRepository energyEfficiencyRepository;
    @Autowired
    public EnergyEfficiencyMeasureService(EnergyEfficiencyMeasureRepository repository,
                                          MessageSource messageSource,
                                          ProjectService projectService,
                                          EnergyEfficiencyRepository energyEfficiencyRepository) {
        super(repository, messageSource);
        this.projectService = projectService;
        this.energyEfficiencyRepository = energyEfficiencyRepository;
    }

    @Override
    public EnergyEfficiencyMeasure update(EnergyEfficiencyMeasure entity, Locale locale) throws ProfitabilityException {
        return null;
    }

    public EnergyEfficiencyMeasure save(long projectID, EemInputData data, Locale locale) throws ProfitabilityException {

        Project project;
        try {
            project  = projectService.findById(projectID, locale);
        } catch (ProfitabilityException e) {
           project = null;
        }

        if (project == null){
            throw new ProfitabilityException(
                    messageSource.getMessage("error.project-not-exist", null, locale)
            );
        }
        EnergyEfficiencyMeasure measure = new EnergyEfficiencyMeasure();
        measure.setInputEEMData(new InputEEMData());

        measure.setName(data.getName());
        measure.getInputEEMData().setAnnualOMCosts(data.getAnnualOMCosts());
        measure.getInputEEMData().setEconomicLifeTime(data.getEconomicLifeTime());
        measure.getInputEEMData().setInitialInvestment(data.getInitialInvestment());

        List<EnergyEfficiency> energyEfficiencies = data.getEnergyEfficiencies();
        energyEfficiencyRepository.saveAll(energyEfficiencies);
        energyEfficiencies.forEach(energyEfficiency -> energyEfficiency.setEem(measure));
        measure.getInputEEMData().setEnergyEfficiencies(energyEfficiencies);

        measure.setResultEEMData(new ResultEEMData());

        measure.getResultEEMData().setInitialSavings(
                calculateInitialSavings(measure.getInputEEMData().getEnergyEfficiencies(), project.getTariffs())
        );
        measure.getResultEEMData().setNetSavings(
                measure.getResultEEMData().getInitialSavings().subtract(measure.getInputEEMData().getAnnualOMCosts())
        );
        measure.getResultEEMData().setPayBack(
                calculatePayBack(measure.getInputEEMData().getInitialInvestment(), measure.getResultEEMData().getNetSavings())
        );



        measure.setProject(project);


        return repository.save(measure);
    }

    private BigDecimal calculatePayBack(BigDecimal initialInvestment, BigDecimal netSavings) {
        return initialInvestment.divide(netSavings,3, RoundingMode.CEILING);
    }

    private BigDecimal calculateInitialSavings(List<EnergyEfficiency> energyEfficiencies, List<EnergyTariff> energyTariffs) {
        BigDecimal result = BigDecimal.valueOf(0);
        for (EnergyEfficiency efficiency : energyEfficiencies){
            EnergyTariff tariff = energyTariffs
                    .stream()
                    .filter(t -> t.getEnergyType().getId() == efficiency.getEnergyType().getId())
                    .findFirst()
                    .get();
            result = result.add(efficiency.getValue().multiply(tariff.getValue()));
        }
        return result;
    }

    public EnergyEfficiencyMeasure update(long eemID, EemInputData data, Locale locale) throws ProfitabilityException {

        Optional<EnergyEfficiencyMeasure> optionalMeasure = repository.findById(eemID);

        if (optionalMeasure.isPresent()){
            EnergyEfficiencyMeasure measure = optionalMeasure.get();
            measure.setName(data.getName());
            measure.getInputEEMData().setAnnualOMCosts(data.getAnnualOMCosts());
            measure.getInputEEMData().setEconomicLifeTime(data.getEconomicLifeTime());
            measure.getInputEEMData().setInitialInvestment(data.getInitialInvestment());

            measure.getInputEEMData().getEnergyEfficiencies()
                    .forEach(
                            energyEfficiency -> energyEfficiency
                                    .setValue(
                                            data.getEnergyEfficiencies()
                                                    .stream()
                                                    .filter(ee -> ee.getEnergyType().equals(energyEfficiency.getEnergyType()))
                                                    .findFirst()
                                                    .get()
                                                    .getValue()
                                    )
                    );

            return repository.save(measure);
        } else {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.project-not-exist", null, locale)
            );
        }
    }
}
