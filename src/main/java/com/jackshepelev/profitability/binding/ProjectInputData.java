package com.jackshepelev.profitability.binding;

import com.jackshepelev.profitability.entity.project.EnergyTariff;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProjectInputData {

    @NotNull
    @NotEmpty(message = "message = *Please provide name of project")
    private String title;

    @NotNull
    @DecimalMax("999.999")
    @DecimalMin("0.001")
    private BigDecimal nominalDiscountRate;

    @NotNull
    @DecimalMax("999.999")
    @DecimalMin("0.001")
    private BigDecimal inflationRate;

    List<EnergyTariff> tariffs;
}
