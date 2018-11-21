package com.jackshepelev.profitability.binding;

import com.jackshepelev.profitability.entity.eem.EnergyEfficiency;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BindingEEMInputData {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.000")
    private BigDecimal initialInvestment;

    @NotNull
    private List<EnergyEfficiency> energyEfficiencies;

    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.000")
    private BigDecimal annualOMCosts;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    private int economicLifeTime;
}
