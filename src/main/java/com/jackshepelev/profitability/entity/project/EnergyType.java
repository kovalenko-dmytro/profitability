package com.jackshepelev.profitability.entity.project;

import com.jackshepelev.profitability.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "energy_types")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyType extends AbstractEntity {

    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "message = *Please provide energy name")
    private String name;
}
