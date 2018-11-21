package com.jackshepelev.profitability.entity.project;

import com.jackshepelev.profitability.entity.AbstractEntity;
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
@Table(name = "energy_tariffs")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyTariff extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "energy_type_id")
    private EnergyType energyType;

    @Column(name = "value", precision=10, scale=3)
    @NotNull
    @DecimalMax("9999999.999")
    @DecimalMin("0.001")
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public EnergyTariff(EnergyType energyType) {
        this.energyType = energyType;
    }
}
