package com.jackshepelev.profitability.entity.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiscountRate {

    @Column(name = "nominal_rate", precision=6, scale=3)
    @NotNull
    @DecimalMax("999.999")
    @DecimalMin("0.001")
    private BigDecimal nominalDiscountRate;

    @Column(name = "inflation_rate", precision=6, scale=3)
    @NotNull
    @DecimalMax("999.999")
    @DecimalMin("0.001")
    private BigDecimal inflationRate;

    @Column(name = "real_rate", precision=6, scale=3)
    private BigDecimal realDiscountRate;
}
