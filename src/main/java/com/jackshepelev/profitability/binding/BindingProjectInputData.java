package com.jackshepelev.profitability.binding;

import com.jackshepelev.profitability.entity.project.EnergyTariff;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BindingProjectInputData {

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @DecimalMax("999.999")
    @DecimalMin("0.001")
    private BigDecimal nominalDiscountRate;

    @NotNull
    @DecimalMax("999.999")
    @DecimalMin("0.001")
    private BigDecimal inflationRate;

    @Valid
    ValidList<EnergyTariff> tariffs;
}
