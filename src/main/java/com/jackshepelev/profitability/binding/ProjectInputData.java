package com.jackshepelev.profitability.binding;

import com.jackshepelev.profitability.entity.project.EnergyTariff;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @NotNull
    @Min(value = 1)
    private int economicLifeTime;

    List<EnergyTariff> tariffs = new ArrayList<>();
}
