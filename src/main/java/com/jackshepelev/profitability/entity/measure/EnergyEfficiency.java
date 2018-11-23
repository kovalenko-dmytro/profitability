package com.jackshepelev.profitability.entity.measure;

import com.jackshepelev.profitability.entity.AbstractEntity;
import com.jackshepelev.profitability.entity.project.EnergyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "eem_energy_efficiencies")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyEfficiency extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "energy_type_id")
    private EnergyType energyType;

    @Column(name = "value", precision=15, scale=3)
    @NotNull
    @DecimalMax("999999999999.999")
    @DecimalMin("0.000")
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "eem_id")
    private EnergyEfficiencyMeasure measure;

    public EnergyEfficiency(EnergyType energyType) {
        this.energyType = energyType;
    }
}
