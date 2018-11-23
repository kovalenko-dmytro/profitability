package com.jackshepelev.profitability.repository.measure;

import com.jackshepelev.profitability.entity.measure.EnergyEfficiency;
import com.jackshepelev.profitability.repository.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnergyEfficiencyRepository
        extends CommonRepository<EnergyEfficiency> {
}
