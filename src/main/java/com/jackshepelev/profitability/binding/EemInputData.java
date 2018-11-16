package com.jackshepelev.profitability.binding;

import com.jackshepelev.profitability.entity.eem.EnergyEfficiency;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class EemInputData {

    private String eemName;
    private BigDecimal initialInvestment;
    private Set<EnergyEfficiency> energyEfficiencySet;
    private BigDecimal annualOMCosts;
    private int economicLifeTime;
    private int projectPackage;
}
