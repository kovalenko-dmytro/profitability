package com.jackshepelev.profitability.entity.project;

import com.jackshepelev.profitability.entity.AbstractEntity;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Project extends AbstractEntity {

    @Column(name = "title", unique = true)
    @NotNull
    @NotEmpty(message = "message = *Please provide name of project")
    private String title;

    @Column(name = "created")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnergyEfficiencyMeasure> eems;
}
