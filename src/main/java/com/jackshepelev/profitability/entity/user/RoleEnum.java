package com.jackshepelev.profitability.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {

    ADMIN("admin"),
    USER("user");

    private String role;
}
