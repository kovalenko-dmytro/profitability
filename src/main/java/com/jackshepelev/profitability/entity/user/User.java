package com.jackshepelev.profitability.entity.user;

import com.jackshepelev.profitability.entity.AbstractEntity;
import com.jackshepelev.profitability.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity {

    @Column(name = "first_name")
    @NotNull
    @NotEmpty(message = "message = *Please provide your first name")
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @NotEmpty(message = "message = *Please provide your last name")
    private String lastName;

    @Column(name = "email")
    @NotNull
    @NotEmpty(message = "message = *Please provide your email")
    @Email(message = "*Please provide a valid Email")
    private String email;

    @Column(name = "password")
    @NotNull
    @NotEmpty(message = "message = *Please provide your password")
    private String password;

    @Column(name = "active")
    private int active;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Project> projects;
}
