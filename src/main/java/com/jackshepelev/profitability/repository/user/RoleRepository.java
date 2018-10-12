package com.jackshepelev.profitability.repository.user;

import com.jackshepelev.profitability.entity.user.Role;
import com.jackshepelev.profitability.repository.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CommonRepository<Role> {

    Role findByRole(String role);
}
