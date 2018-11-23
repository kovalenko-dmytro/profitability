package com.jackshepelev.profitability.entity.measure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InputEEMData {

    @Column(name = "initial_investment", precision=15, scale=3)
    @NotNull
    @NotEmpty(message = "message = *Please provide initial investments value")
    @DecimalMax("999999999999.999")
    @DecimalMin("0.000")
    private BigDecimal initialInvestment;

    @OneToMany(mappedBy = "measure", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<EnergyEfficiency> energyEfficiencies;

    @Column(name = "annual_om_costs", precision=12, scale=3)
    @NotNull
    @NotEmpty(message = "message = *Please provide annual operation and maintain costs value")
    @DecimalMax("999999999.999")
    @DecimalMin("0.000")
    private BigDecimal annualOMCosts;

    @Column(name = "life_time")
    @NotNull
    @NotEmpty(message = "message = *Please provide economic lifetime value")
    @Min(value = 1)
    @Max(value = 100)
    private int economicLifeTime;
}
