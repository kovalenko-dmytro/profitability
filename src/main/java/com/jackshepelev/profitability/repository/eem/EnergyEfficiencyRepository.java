package com.jackshepelev.profitability.repository.eem;

import com.jackshepelev.profitability.entity.eem.EnergyEfficiency;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.entity.project.EnergyType;
import com.jackshepelev.profitability.repository.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyEfficiencyRepository
        extends CommonRepository<EnergyEfficiency> {

    EnergyEfficiency findByEem(EnergyEfficiencyMeasure measure);
    EnergyEfficiency findByEemAndAndEnergyType(EnergyEfficiencyMeasure measure, EnergyType type);
}
