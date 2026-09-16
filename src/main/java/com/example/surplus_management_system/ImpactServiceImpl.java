package com.example.surplus_management_system;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class ImpactServiceImpl implements ImpactService {

    private final ImpactRecordRepository impactRecordRepository;
    private final RequestRepository requestRepository;

    public ImpactServiceImpl(
            ImpactRecordRepository impactRecordRepository,
            RequestRepository requestRepository
    ) {
        this.impactRecordRepository = impactRecordRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public ImpactResponse getOverallImpact() {

        List<ImpactRecord> records =
                impactRecordRepository.findAll();

        return calculateImpact(records);
    }

    @Override
    public ImpactResponse getOrganizationImpact(
            UUID organizationId
    ) {
        if (organizationId == null) {
            throw new IllegalArgumentException(
                    "Organization ID cannot be null"
            );
        }

        List<ImpactRecord> records =
                impactRecordRepository.findByOrganizationId(
                        organizationId
                );

        return calculateImpact(records);
    }

    @Override
    public ImpactRecord createImpactRecord(
            ImpactRecord impactRecord
    ) {
        if (impactRecord == null) {
            throw new IllegalArgumentException(
                    "Impact record cannot be null"
            );
        }

        if (impactRecord.getOrganizationId() == null) {
            throw new IllegalArgumentException(
                    "Organization ID cannot be null"
            );
        }

        if (impactRecord.getResourceCategory() == null) {
            throw new IllegalArgumentException(
                    "Resource category cannot be null"
            );
        }

        if (impactRecord.getQuantityRedistributed() == null
                || impactRecord.getQuantityRedistributed()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Quantity redistributed must be greater than zero"
            );
        }

        if (impactRecord.getBeneficiariesEstimated() != null
                && impactRecord.getBeneficiariesEstimated() < 0) {

            throw new IllegalArgumentException(
                    "Estimated beneficiaries cannot be negative"
            );
        }

        if (impactRecord.getWasteDivertedKg() != null
                && impactRecord.getWasteDivertedKg() < 0) {

            throw new IllegalArgumentException(
                    "Waste diverted cannot be negative"
            );
        }

        return impactRecordRepository.save(impactRecord);
    }

    @Override
    public List<ImpactRecord> getOrganizationImpactRecords(
            UUID organizationId
    ) {
        if (organizationId == null) {
            throw new IllegalArgumentException(
                    "Organization ID cannot be null"
            );
        }

        return impactRecordRepository.findByOrganizationId(
                organizationId
        );
    }

    private ImpactResponse calculateImpact(
            List<ImpactRecord> records
    ) {
        if (records == null || records.isEmpty()) {
            return ImpactResponse.builder()
                    .totalResourcesRedistributed(0.0)
                    .organizationsSupported(0)
                    .completedMatches(0)
                    .estimatedBeneficiaries(0)
                    .estimatedWasteDivertedKg(0.0)
                    .build();
        }

        double totalResourcesRedistributed = records.stream()
                .map(ImpactRecord::getQuantityRedistributed)
                .filter(quantity -> quantity != null)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        Set<UUID> organizations = new HashSet<>();

        for (ImpactRecord record : records) {
            if (record.getOrganizationId() != null) {
                organizations.add(record.getOrganizationId());
            }
        }

        int estimatedBeneficiaries = records.stream()
                .map(ImpactRecord::getBeneficiariesEstimated)
                .filter(value -> value != null)
                .mapToInt(Integer::intValue)
                .sum();

        double estimatedWasteDivertedKg = records.stream()
                .map(ImpactRecord::getWasteDivertedKg)
                .filter(value -> value != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        int completedMatches = countCompletedRequests(records);

        return ImpactResponse.builder()
                .totalResourcesRedistributed(
                        totalResourcesRedistributed
                )
                .organizationsSupported(
                        organizations.size()
                )
                .completedMatches(
                        completedMatches
                )
                .estimatedBeneficiaries(
                        estimatedBeneficiaries
                )
                .estimatedWasteDivertedKg(
                        estimatedWasteDivertedKg
                )
                .build();
    }

    private int countCompletedRequests(
            List<ImpactRecord> records
    ) {

        Set<UUID> requestIds = new HashSet<>();

        for (ImpactRecord record : records) {

            if (record.getRequestId() != null) {
                requestIds.add(record.getRequestId());
            }
        }

        if (requestIds.isEmpty()) {
            return 0;
        }

        int completed = 0;

        for (UUID requestId : requestIds) {

            Request request =
                    requestRepository.findById(requestId)
                            .orElse(null);

            if (request != null
                    && request.getStatus() == RequestStatus.COMPLETED) {

                completed++;
            }
        }

        return completed;
    }

}
