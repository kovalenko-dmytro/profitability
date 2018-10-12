package com.jackshepelev.profitability.entity.user;

import com.jackshepelev.profitability.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Component("role")
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractEntity {

    @Column(name = "role")
    private String role;
}
