package com.jackshepelev.profitability.entity.eem;

import com.jackshepelev.profitability.entity.AbstractEntity;
import com.jackshepelev.profitability.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Component("energyEfficiencyMeasure")
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

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
