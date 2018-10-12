package com.jackshepelev.profitability.repository.user;

import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.repository.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonRepository<User> {

    User findByEmail(String email);
}
