package com.jackshepelev.profitability.entity.eem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InputEEMData {

    @Column(name = "initial_investment", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.001")
    private BigDecimal initialInvestment;

    @OneToMany(mappedBy = "eem", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<EnergyEfficiency> energyEfficiencySet;

    @Column(name = "annual_om_costs", precision=12, scale=3)
    @NotNull
    @DecimalMax("999999999.999")
    @DecimalMin("0.001")
    private BigDecimal annualOMCosts;

    @Column(name = "package")
    @Min(value = 1)
    @Max(value = 5)
    private int projectPackage;
}
