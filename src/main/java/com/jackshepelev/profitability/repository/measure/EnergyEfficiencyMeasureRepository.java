package com.jackshepelev.profitability.repository.measure;

import com.jackshepelev.profitability.entity.measure.EnergyEfficiencyMeasure;
import com.jackshepelev.profitability.repository.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyEfficiencyMeasureRepository
        extends CommonRepository<EnergyEfficiencyMeasure> {
}
