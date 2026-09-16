package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface ImpactService {

    ImpactResponse getOverallImpact();

    ImpactResponse getOrganizationImpact(UUID organizationId);

    ImpactRecord createImpactRecord(ImpactRecord impactRecord);

    List<ImpactRecord> getOrganizationImpactRecords(UUID organizationId);
}