package com.jackshepelev.profitability.service.project;

import com.jackshepelev.profitability.binding.BindingProjectInputData;
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
public class ProjectService
        extends AbstractService<Project, ProjectRepository> {

    private final EnergyTariffRepository energyTariffRepository;

    @Autowired
    public ProjectService(ProjectRepository repository,
                          MessageSource messageSource,
                          EnergyTariffRepository energyTariffRepository) {

        super(repository, messageSource);

        this.energyTariffRepository = energyTariffRepository;
    }

    public Project save(User user, BindingProjectInputData data) {

        Project project = new Project();
        project.setTitle(data.getTitle());
        project.setDate(LocalDateTime.now());
        project.setUser(user);

        BigDecimal realDiscountRate = (
                (BigDecimal.valueOf(1).add(data.getNominalDiscountRate().divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING)))
                        .divide(
                                (BigDecimal.valueOf(1).add(data.getInflationRate().divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING))),
                                3,
                                RoundingMode.CEILING
                        )
        ).subtract(BigDecimal.valueOf(1));

        project.setDiscountRate(
                new DiscountRate(
                        data.getNominalDiscountRate().divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING),
                        data.getInflationRate().divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING),
                        realDiscountRate
                )
        );

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
