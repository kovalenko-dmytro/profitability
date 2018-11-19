package com.jackshepelev.profitability.service.project;

import com.jackshepelev.profitability.binding.ProjectInputData;
import com.jackshepelev.profitability.entity.project.DiscountRate;
import com.jackshepelev.profitability.entity.project.EnergyTariff;
import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.project.EnergyTariffRepository;
import com.jackshepelev.profitability.repository.project.ProjectRepository;
import com.jackshepelev.profitability.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ProjectService extends AbstractService<Project, ProjectRepository> {

    private final EnergyTariffRepository energyTariffRepository;

    @Autowired
    public ProjectService(ProjectRepository repository,
                          MessageSource messageSource,
                          EnergyTariffRepository energyTariffRepository) {

        super(repository, messageSource);

        this.energyTariffRepository = energyTariffRepository;
    }

    public Project save(User user, ProjectInputData data) {

        Project project = new Project();
        project.setTitle(data.getTitle());
        project.setDate(LocalDateTime.now());
        project.setUser(user);

        BigDecimal realDiscountRate = (data.getNominalDiscountRate().subtract(data.getInflationRate()))
                .divide(data.getInflationRate().add(BigDecimal.valueOf(1)), 3, RoundingMode.CEILING);

        project.setDiscountRate(new DiscountRate(data.getNominalDiscountRate(), data.getInflationRate(), realDiscountRate));

        List<EnergyTariff> tariffs = data.getTariffs();
        energyTariffRepository.saveAll(tariffs);
        tariffs.forEach(tariff -> tariff.setProject(project));
        project.setTariffs(tariffs);

        return repository.save(project);
    }

    @Override
    public Project update(Project entity, Locale locale) throws ProfitabilityException {
        return null;
    }
}
