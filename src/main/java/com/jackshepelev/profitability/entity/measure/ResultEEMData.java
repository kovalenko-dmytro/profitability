package com.jackshepelev.profitability.entity.measure;

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
public class ResultEEMData {

    @Column(name = "init_savings", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.001")
    private BigDecimal initialSavings;

    @Column(name = "net_savings", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.001")
    private BigDecimal netSavings;

    @Column(name = "pay_back", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.001")
    private BigDecimal payBack;

    @Column(name = "pay_off", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.001")
    private BigDecimal payOff;

    @Column(name = "irr", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.001")
    private BigDecimal internalRateOfReturn;

    @Column(name = "npv", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.001")
    private BigDecimal netPresentValue;

    @Column(name = "npvq", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.001")
    private BigDecimal netPresentValueQuotient;
}
