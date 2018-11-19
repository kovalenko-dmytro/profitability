package com.jackshepelev.profitability.binding;

import com.jackshepelev.profitability.entity.eem.EnergyEfficiency;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EemInputData {

    private String name;
    private BigDecimal initialInvestment;
    private List<EnergyEfficiency> energyEfficiencies;
    private BigDecimal annualOMCosts;
    private int economicLifeTime;
}
