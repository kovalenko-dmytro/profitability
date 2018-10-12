package com.jackshepelev.profitability.service.user;

import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.user.UserRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService extends AbstractService<User, UserRepository> {

    @Autowired
    public UserService(UserRepository repository, MessageSource messageSource) {
        super(repository, messageSource);
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
