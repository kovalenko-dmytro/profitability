package com.jackshepelev.profitability.service.eem;

import com.jackshepelev.profitability.binding.BindingEEMInputData;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiency;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.entity.project.EnergyTariff;
import com.jackshepelev.profitability.entity.project.Project;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IndicatorsEEMService {

    public void setInputData(EnergyEfficiencyMeasure measure, BindingEEMInputData data) {
        measure.setName(data.getName());
        measure.getInputEEMData().setAnnualOMCosts(data.getAnnualOMCosts());
        measure.getInputEEMData().setEconomicLifeTime(data.getEconomicLifeTime());
        measure.getInputEEMData().setInitialInvestment(data.getInitialInvestment());
    }

    public void calculateResultData(EnergyEfficiencyMeasure measure, Project project) {

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

        measure.getResultEEMData().setInternalRateOfReturn(
                calculateIRR(
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

    private BigDecimal calculateIRR(BigDecimal netSavings,
                                    BigDecimal realDiscountRate,
                                    int economicLifeTime,
                                    BigDecimal initialInvestment) {

        BigDecimal npv = calculateNPV(
                netSavings,
                realDiscountRate,
                economicLifeTime,
                initialInvestment
        );
        BigDecimal result = realDiscountRate;
        while (npv.abs().compareTo(BigDecimal.valueOf(0)) > 0 ) {
            if (npv.compareTo(BigDecimal.valueOf(0)) > 0) {
                result = result.add(realDiscountRate.divide(BigDecimal.valueOf(200), 3, RoundingMode.CEILING));
                npv = calculateNPV(
                        netSavings,
                        result,
                        economicLifeTime,
                        initialInvestment
                );
                if (npv.compareTo(BigDecimal.valueOf(0)) < 0) {break;}
            } else {
                result = result.subtract(realDiscountRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING));
                npv = calculateNPV(
                        netSavings,
                        result,
                        economicLifeTime,
                        initialInvestment
                );
                if (npv.compareTo(BigDecimal.valueOf(0)) > 0) {break;}
            }
        }
        return result.multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal calculateInitialSavings(List<EnergyEfficiency> energyEfficiencies,
                                               List<EnergyTariff> energyTariffs) {

        BigDecimal result = BigDecimal.valueOf(0);
        for (EnergyEfficiency efficiency : energyEfficiencies){
            if (efficiency.getValue() == null) {continue;}
            Optional<EnergyTariff> optionalTariff = energyTariffs
                    .stream()
                    .filter(t -> t.getEnergyType().getId() == efficiency.getEnergyType().getId())
                    .findFirst();
            if (optionalTariff.isPresent()) {
                EnergyTariff tariff = optionalTariff.get();
                result = result.add(efficiency.getValue().multiply(tariff.getValue()));
            }
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
