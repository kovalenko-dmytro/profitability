package com.jackshepelev.profitability.service.eem;

import com.jackshepelev.profitability.binding.BindingEEMInputData;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class EnergyEfficiencyMeasureService
        extends AbstractService<EnergyEfficiencyMeasure, EnergyEfficiencyMeasureRepository> {

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

    public EnergyEfficiencyMeasure save(long projectID,
                                        BindingEEMInputData data,
                                        Locale locale) throws ProfitabilityException {

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

        setInputData(measure, data);

        List<EnergyEfficiency> energyEfficiencies = data.getEnergyEfficiencies();
        energyEfficiencyRepository.saveAll(energyEfficiencies);
        energyEfficiencies.forEach(energyEfficiency -> energyEfficiency.setEem(measure));
        measure.getInputEEMData().setEnergyEfficiencies(energyEfficiencies);

        measure.setResultEEMData(new ResultEEMData());

        calculateResultData(measure, project);

        measure.setProject(project);
        return repository.save(measure);
    }

    public EnergyEfficiencyMeasure update(long eemID,
                                          BindingEEMInputData data,
                                          Locale locale) throws ProfitabilityException {

        Optional<EnergyEfficiencyMeasure> optionalMeasure = repository.findById(eemID);

        if (optionalMeasure.isPresent()){
            EnergyEfficiencyMeasure measure = optionalMeasure.get();
            Project project = projectService.findById(measure.getProject().getId(), locale);

            setInputData(measure, data);

            measure.getInputEEMData().getEnergyEfficiencies()
                    .forEach(
                            energyEfficiency -> energyEfficiency
                                    .setValue(
                                            data.getEnergyEfficiencies()
                                                    .stream()
                                                    .filter(
                                                            ee -> ee.getEnergyType().equals(energyEfficiency.getEnergyType())
                                                    )
                                                    .findFirst()
                                                    .get()
                                                    .getValue()
                                    )
                    );

            calculateResultData(measure, project);

            return repository.save(measure);
        } else {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.project-not-exist", null, locale)
            );
        }
    }

    private void setInputData(EnergyEfficiencyMeasure measure, BindingEEMInputData data) {
        measure.setName(data.getName());
        measure.getInputEEMData().setAnnualOMCosts(data.getAnnualOMCosts());
        measure.getInputEEMData().setEconomicLifeTime(data.getEconomicLifeTime());
        measure.getInputEEMData().setInitialInvestment(data.getInitialInvestment());
    }

    private void calculateResultData(EnergyEfficiencyMeasure measure, Project project) {

        measure.getResultEEMData().setInitialSavings(
                calculateInitialSavings(
                        measure.getInputEEMData().getEnergyEfficiencies(),
                        project.getTariffs()
                )
        );
        measure.getResultEEMData().setNetSavings(
                calculateNetSavings(
                        measure.getResultEEMData().getInitialSavings(),
                        measure.getInputEEMData().getAnnualOMCosts()
                )
        );
        measure.getResultEEMData().setPayBack(
                calculatePayBack(
                        measure.getInputEEMData().getInitialInvestment(),
                        measure.getResultEEMData().getNetSavings()
                )
        );
        measure.getResultEEMData().setPayOff(
                calculatePayOff(
                        measure.getResultEEMData().getNetSavings(),
                        measure.getInputEEMData().getInitialInvestment(),
                        project.getDiscountRate().getRealDiscountRate(),
                        measure.getInputEEMData().getEconomicLifeTime()
                )
        );
        measure.getResultEEMData().setNetPresentValue(
                calculateNPV(
                        measure.getResultEEMData().getNetSavings(),
                        project.getDiscountRate().getRealDiscountRate(),
                        measure.getInputEEMData().getEconomicLifeTime(),
                        measure.getInputEEMData().getInitialInvestment()
                )
        );
        measure.getResultEEMData().setNetPresentValueQuotient(
                calculateNPVQ(
                        measure.getResultEEMData().getNetSavings(),
                        project.getDiscountRate().getRealDiscountRate(),
                        measure.getInputEEMData().getEconomicLifeTime(),
                        measure.getInputEEMData().getInitialInvestment()
                )
        );
    }

    private BigDecimal calculateInitialSavings(List<EnergyEfficiency> energyEfficiencies,
                                               List<EnergyTariff> energyTariffs) {

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

    private BigDecimal calculateNetSavings(BigDecimal initialSavings,
                                           BigDecimal annualOMCosts) {

        return initialSavings.subtract(annualOMCosts);
    }

    private BigDecimal calculatePayBack(BigDecimal initialInvestment,
                                        BigDecimal netSavings) {

        return initialInvestment.divide(netSavings,3, RoundingMode.CEILING);
    }

    private BigDecimal calculatePayOff(BigDecimal netSavings,
                                       BigDecimal initialInvestment,
                                       BigDecimal realDiscountRate,
                                       int economicLifeTime) {

        List<BigDecimal> npvList =calculateNPVList(netSavings, realDiscountRate, economicLifeTime);

        BigDecimal sum = BigDecimal.valueOf(0);
        BigDecimal currentValue = null;
        int index = 0;
        for (int i = 0; i < npvList.size(); i++) {
            sum = sum.add(npvList.get(i));
            currentValue = npvList.get(i);
            index = i;
            if (sum.compareTo(initialInvestment) >= 0) {
                break;
            }
        }

        return  BigDecimal.valueOf(index)
                .add(
                        BigDecimal.valueOf(1)
                                .subtract(
                                        (sum.subtract(initialInvestment))
                                                .divide(currentValue, 3, RoundingMode.CEILING)
                                )
                );
    }

    private BigDecimal calculateNPV(BigDecimal netSavings,
                                    BigDecimal realDiscountRate,
                                    int economicLifeTime,
                                    BigDecimal initialInvestment) {

        List<BigDecimal> npvList =calculateNPVList(netSavings, realDiscountRate, economicLifeTime);
        BigDecimal result = BigDecimal.valueOf(0);
        for (BigDecimal value : npvList) {
            result = result.add(value);
        }
        return result.subtract(initialInvestment);
    }

    private BigDecimal calculateNPVQ(BigDecimal netSavings,
                                     BigDecimal realDiscountRate,
                                     int economicLifeTime,
                                     BigDecimal initialInvestment) {

        BigDecimal npv =calculateNPV(netSavings, realDiscountRate, economicLifeTime,initialInvestment);
        return npv.divide(initialInvestment, 3, RoundingMode.CEILING);
    }

    private List<BigDecimal> calculateNPVList(BigDecimal netSavings,
                                              BigDecimal realDiscountRate,
                                              int economicLifeTime) {

        List<BigDecimal> result = new ArrayList<>();
        BigDecimal yearValue;
        for (int i = 1; i <= economicLifeTime; i++) {
            yearValue = netSavings
                    .divide((BigDecimal.valueOf(1).add(realDiscountRate)).pow(i), 3, RoundingMode.CEILING);
            result.add(yearValue);
        }
        return result;
    }
}
