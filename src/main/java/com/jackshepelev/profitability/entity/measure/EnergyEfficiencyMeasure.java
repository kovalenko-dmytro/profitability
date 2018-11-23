package com.jackshepelev.profitability.entity.measure;

import com.jackshepelev.profitability.entity.AbstractEntity;
import com.jackshepelev.profitability.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "eems")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyEfficiencyMeasure extends AbstractEntity {

    @Column(name = "name")
    @NotNull
    @NotEmpty(message = "message = *Please provide name of EEM")
    private String name;

    @Embedded
    private InputEEMData inputEEMData;

    @Embedded
    private ResultEEMData resultEEMData;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
