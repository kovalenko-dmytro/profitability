package com.jackshepelev.profitability.service.project;

import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.project.ProjectRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@Transactional
public class ProjectService extends AbstractService<Project, ProjectRepository> {

    @Autowired
    public ProjectService(ProjectRepository repository, MessageSource messageSource) {
        super(repository, messageSource);
    }

    public Project save(User user, Project entity) {
        entity.setDate(LocalDateTime.now());
        entity.setUser(user);
        repository.save(entity);
        return repository.findByTitle(entity.getTitle());
    }

    @Override
    public Project update(Project entity, Locale locale) throws ProfitabilityException {
        return null;
    }
}
