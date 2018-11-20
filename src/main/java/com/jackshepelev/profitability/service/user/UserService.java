package com.jackshepelev.profitability.service.user;

import com.jackshepelev.profitability.entity.user.Role;
import com.jackshepelev.profitability.entity.user.RoleEnum;
import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.user.RoleRepository;
import com.jackshepelev.profitability.repository.user.UserRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService
        extends AbstractService<User, UserRepository> {

    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository,
                       MessageSource messageSource,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder encoder) {

        super(repository, messageSource);
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public User save(User entity) {

        entity.setPassword(encoder.encode(entity.getPassword()));
        entity.setActive(1);

        Role userRole = roleRepository.findByRole(RoleEnum.USER.getRole());
        entity.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        repository.save(entity);

        return entity;
    }

    public User findByEmail(String email) {

        return repository.findByEmail(email);
    }

    @Override
    public User update(User entity, Locale locale) throws ProfitabilityException {

        Optional<User> optionalUser = repository.findById(entity.getId());

        if (optionalUser.isPresent()) {

            User updateUser = optionalUser.get();
            updateUser.setFirstName(entity.getFirstName());
            updateUser.setLastName(entity.getLastName());
            updateUser.setEmail(entity.getEmail());
            updateUser.setActive(entity.getActive());

            return repository.save(updateUser);

        } else {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.user-not-exist", null, locale)
            );
        }
    }
}
