package com.jackshepelev.profitability.service.project;

import com.jackshepelev.profitability.binding.BindingProjectInputData;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.entity.project.DiscountRate;
import com.jackshepelev.profitability.entity.project.EnergyTariff;
import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.repository.eem.EnergyEfficiencyMeasureRepository;
import com.jackshepelev.profitability.repository.project.EnergyTariffRepository;
import com.jackshepelev.profitability.repository.project.ProjectRepository;
import com.jackshepelev.profitability.service.AbstractService;
import com.jackshepelev.profitability.service.eem.IndicatorsEEMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class ProjectService
        extends AbstractService<Project, ProjectRepository> {

    private final EnergyTariffRepository energyTariffRepository;
    private final IndicatorsEEMService indicatorsEEMService;
    private final EnergyEfficiencyMeasureRepository energyEfficiencyMeasureRepository;

    @Autowired
    public ProjectService(ProjectRepository repository,
                          MessageSource messageSource,
                          EnergyTariffRepository energyTariffRepository,
                          IndicatorsEEMService indicatorsEEMService,
                          EnergyEfficiencyMeasureRepository energyEfficiencyMeasureRepository) {

        super(repository, messageSource);

        this.energyTariffRepository = energyTariffRepository;
        this.indicatorsEEMService = indicatorsEEMService;
        this.energyEfficiencyMeasureRepository = energyEfficiencyMeasureRepository;
    }

    public Project save(User user, BindingProjectInputData data) {

        Project project = new Project();
        project.setDate(LocalDateTime.now());
        project.setUser(user);
        setProjectData(project, data);

        return repository.save(project);
    }

    @Override
    public Project update(Project entity, Locale locale) throws ProfitabilityException {
        return null;
    }

    public Project update(long id,
                          BindingProjectInputData data,
                          Locale locale) throws ProfitabilityException {

        Optional<Project> optionalProject = repository.findById(id);

        if (!optionalProject.isPresent()) {
            throw new ProfitabilityException(
                    messageSource.getMessage("error.project-not-exist", null, locale)
            );
        }

        Project project = optionalProject.get();
        setProjectData(project, data);
        repository.save(project);

        List<EnergyEfficiencyMeasure> eems = project.getEems();
        if (eems.size() > 0) {
            eems.forEach(eem -> indicatorsEEMService.calculateResultData(eem, project));
        }
        energyEfficiencyMeasureRepository.saveAll(eems);

        return  project;
    }

    private void setProjectData(Project project, BindingProjectInputData data) {
        project.setTitle(data.getTitle());
        BigDecimal realDiscountRate = calculateRealDiscountRate(data);
        project.setDiscountRate(
                new DiscountRate(
                        data.getNominalDiscountRate().divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING),
                        data.getInflationRate().divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING),
                        realDiscountRate
                )
        );

        if (project.getId() != 0) {
            project.getTariffs().forEach(energyTariff -> {
                energyTariff.setValue(
                        data.getTariffs()
                                .stream()
                                .filter(dataTariff -> dataTariff.getEnergyType().getId() == energyTariff.getEnergyType().getId())
                                .findFirst()
                                .get()
                                .getValue()
                );
            });
        } else {
            List<EnergyTariff> tariffs = data.getTariffs();
            energyTariffRepository.saveAll(tariffs);
            tariffs.forEach(tariff -> tariff.setProject(project));
        }
    }

    private BigDecimal calculateRealDiscountRate(BindingProjectInputData data) {
        return ((BigDecimal.valueOf(1).add(data.getNominalDiscountRate().divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING)))
                .divide((BigDecimal.valueOf(1).add(data.getInflationRate().divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING))),3, RoundingMode.CEILING))
                .subtract(BigDecimal.valueOf(1));
    }
}
