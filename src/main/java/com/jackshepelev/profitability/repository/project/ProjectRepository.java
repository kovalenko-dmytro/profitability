package com.jackshepelev.profitability.repository.project;

import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.repository.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository
        extends CommonRepository<Project> {
}
