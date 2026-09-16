package com.example.surplus_management_system;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchingServiceImpl implements MatchingService {

    private final MatchRepository matchRepository;
    private final SurplusRepository surplusRepository;
    private final NeedRepository needRepository;

    public MatchingServiceImpl(
            MatchRepository matchRepository,
            SurplusRepository surplusRepository,
            NeedRepository needRepository
    ) {
        this.matchRepository = matchRepository;
        this.surplusRepository = surplusRepository;
        this.needRepository = needRepository;
    }

    @Override
    public MatchScoreResponse calculateMatchScore(
            UUID surplusId,
            UUID needId
    ) {

        Surplus surplus = findSurplus(surplusId);
        Need need = findNeed(needId);

        double categoryScore =
                calculateCategoryScore(surplus, need);

        double quantityScore =
                calculateQuantityScore(surplus, need);

        double distanceKm =
                calculateDistanceKm(
                        surplus.getLatitude(),
                        surplus.getLongitude(),
                        need.getLatitude(),
                        need.getLongitude()
                );

        double distanceScore =
                calculateDistanceScore(distanceKm);

        double urgencyScore =
                calculateUrgencyScore(need.getUrgency());

        double conditionScore =
                calculateConditionScore(String.valueOf(surplus.getCondition()));

        double availabilityScore =
                calculateAvailabilityScore(surplus);

        double overallScore =
                categoryScore
                        + quantityScore
                        + distanceScore
                        + urgencyScore
                        + conditionScore
                        + availabilityScore;

        return MatchScoreResponse.builder()
                .overallScore(round(overallScore))
                .categoryScore(round(categoryScore))
                .quantityScore(round(quantityScore))
                .distanceScore(round(distanceScore))
                .urgencyScore(round(urgencyScore))
                .conditionScore(round(conditionScore))
                .availabilityScore(round(availabilityScore))
                .distanceKm(round(distanceKm))
                .build();
    }
    @Override
    public MatchResponse createMatch(
            UUID surplusId,
            UUID needId
    ) {

        Surplus surplus = findSurplus(surplusId);
        Need need = findNeed(needId);

        Match existingMatch =
                matchRepository.findBySurplusIdAndNeedIdAndStatus(
                        surplusId,
                        needId,
                        MatchStatus.ACTIVE
                ).orElse(null);

        if (existingMatch != null) {

            if (existingMatch.getExpiresAt() != null
                    && existingMatch.getExpiresAt()
                    .isBefore(LocalDateTime.now())) {

                existingMatch.setStatus(MatchStatus.EXPIRED);
                matchRepository.save(existingMatch);

            } else {
                return mapToResponse(existingMatch);
            }
        }

        MatchScoreResponse score =
                calculateMatchScore(
                        surplusId,
                        needId
                );

        Match match = Match.builder()
                .surplusId(surplusId)
                .needId(needId)
                .overallScore(score.getOverallScore())
                .categoryScore(score.getCategoryScore())
                .quantityScore(score.getQuantityScore())
                .distanceScore(score.getDistanceScore())
                .urgencyScore(score.getUrgencyScore())
                .conditionScore(score.getConditionScore())
                .availabilityScore(score.getAvailabilityScore())
                .distanceKm(score.getDistanceKm())
                .matchReason(
                        generateMatchReason(
                                surplus,
                                need,
                                score
                        )
                )
                .status(MatchStatus.ACTIVE)
                .expiresAt(
                        calculateExpiryTime(surplus)
                )
                .build();

        Match savedMatch =
                matchRepository.save(match);

        return mapToResponse(savedMatch);
    }


    @Override
    public List<MatchResponse> findBestMatchesForSurplus(
            UUID surplusId
    ) {

        findSurplus(surplusId);

        List<Need> openNeeds =
                needRepository.findByStatus(
                        NeedStatus.OPEN
                );

        return openNeeds.stream()
                .map(need ->
                        createOrGetMatch(
                                surplusId,
                                need.getId()
                        )
                )
                .sorted(
                        (a, b) ->
                                Double.compare(
                                        b.getOverallScore(),
                                        a.getOverallScore()
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchResponse> findBestMatchesForNeed(
            UUID needId
    ) {

        findNeed(needId);

        List<Surplus> availableSurplus =
                surplusRepository.findByStatus(
                        SurplusStatus.AVAILABLE
                );

        return availableSurplus.stream()
                .map(surplus ->
                        createOrGetMatch(
                                surplus.getId(),
                                needId
                        )
                )
                .sorted(
                        (a, b) ->
                                Double.compare(
                                        b.getOverallScore(),
                                        a.getOverallScore()
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    public MatchResponse getMatch(UUID id) {

        Match match =
                matchRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Match not found with id: " + id
                                )
                        );

        return mapToResponse(match);
    }

    @Override
    public List<MatchResponse> getAllActiveMatches() {

        return matchRepository
                .findByStatus(MatchStatus.ACTIVE)
                .stream()
                .sorted(
                        (a, b) ->
                                Double.compare(
                                        b.getOverallScore(),
                                        a.getOverallScore()
                                )
                )
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void expireMatch(UUID id) {

        Match match =
                matchRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Match not found with id: " + id
                                )
                        );

        match.setStatus(MatchStatus.EXPIRED);

        matchRepository.save(match);
    }

    private MatchResponse createOrGetMatch(
            UUID surplusId,
            UUID needId
    ) {

        Match existingMatch =
                matchRepository.findBySurplusIdAndNeedIdAndStatus(
                        surplusId,
                        needId,
                        MatchStatus.ACTIVE
                ).orElse(null);

        if (existingMatch != null) {

            if (existingMatch.getExpiresAt() != null
                    && existingMatch.getExpiresAt()
                    .isBefore(LocalDateTime.now())) {

                existingMatch.setStatus(MatchStatus.EXPIRED);
                matchRepository.save(existingMatch);

            } else {
                return mapToResponse(existingMatch);
            }
        }

        Surplus surplus = findSurplus(surplusId);
        Need need = findNeed(needId);

        MatchScoreResponse score =
                calculateMatchScore(
                        surplusId,
                        needId
                );

        Match match = Match.builder()
                .surplusId(surplusId)
                .needId(needId)
                .overallScore(score.getOverallScore())
                .categoryScore(score.getCategoryScore())
                .quantityScore(score.getQuantityScore())
                .distanceScore(score.getDistanceScore())
                .urgencyScore(score.getUrgencyScore())
                .conditionScore(score.getConditionScore())
                .availabilityScore(score.getAvailabilityScore())
                .distanceKm(score.getDistanceKm())
                .matchReason(
                        generateMatchReason(
                                surplus,
                                need,
                                score
                        )
                )
                .status(MatchStatus.ACTIVE)
                .expiresAt(
                        calculateExpiryTime(surplus)
                )
                .build();

        return mapToResponse(
                matchRepository.save(match)
        );
    }


    private double calculateCategoryScore(
            Surplus surplus,
            Need need
    ) {

        if (surplus.getCategory() == null
                || need.getCategory() == null) {
            return 0;
        }

        return surplus.getCategory()
                == need.getCategory()
                ? 30.0
                : 0.0;
    }

    private double calculateQuantityScore(
            Surplus surplus,
            Need need
    ) {

        if (surplus.getQuantity() == null
                || need.getQuantityRequired() == null) {
            return 0;
        }

        BigDecimal available =
                surplus.getQuantity();

        BigDecimal required =
                need.getQuantityRequired();

        if (available.compareTo(BigDecimal.ZERO) <= 0
                || required.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        if (available.compareTo(required) >= 0) {
            return 20.0;
        }

        double ratio =
                available.doubleValue()
                        / required.doubleValue();

        return Math.max(
                0,
                Math.min(20.0, ratio * 20.0)
        );
    }


    private double calculateDistanceScore(
            double distanceKm
    ) {

        // Location unavailable.
        // Do not treat missing coordinates as zero distance.
        if (distanceKm < 0) {
            return 0.0;
        }

        if (distanceKm < 5) {
            return 20.0;
        }

        if (distanceKm <= 15) {
            return 15.0;
        }

        if (distanceKm <= 30) {
            return 10.0;
        }

        return 5.0;
    }



    private double calculateUrgencyScore(
            UrgencyLevel urgency
    ) {

        if (urgency == null) {
            return 0;
        }

        return switch (urgency) {
            case CRITICAL -> 15.0;
            case HIGH -> 12.0;
            case MEDIUM -> 8.0;
            case LOW -> 4.0;
        };
    }

    private double calculateConditionScore(
            String condition
    ) {

        if (condition == null
                || condition.isBlank()) {
            return 5.0;
        }

        String normalized =
                condition.trim().toLowerCase();

        if (normalized.contains("excellent")
                || normalized.contains("new")) {
            return 10.0;
        }

        if (normalized.contains("good")
                || normalized.contains("working")) {
            return 8.0;
        }

        if (normalized.contains("fair")) {
            return 6.0;
        }

        if (normalized.contains("used")) {
            return 5.0;
        }

        if (normalized.contains("poor")) {
            return 2.0;
        }

        return 5.0;
    }

    private double calculateAvailabilityScore(
            Surplus surplus
    ) {

        LocalDateTime now =
                LocalDateTime.now();

        if (surplus.getAvailableUntil() == null) {
            return 5.0;
        }

        if (surplus.getAvailableUntil()
                .isBefore(now)) {
            return 0.0;
        }

        long hours =
                ChronoUnit.HOURS.between(
                        now,
                        surplus.getAvailableUntil()
                );

        if (hours <= 24) {
            return 5.0;
        }

        if (hours <= 72) {
            return 4.0;
        }

        if (hours <= 168) {
            return 3.0;
        }

        return 2.0;
    }

    private double calculateDistanceKm(
            Double latitude1,
            Double longitude1,
            Double latitude2,
            Double longitude2
    ) {

        if (latitude1 == null
                || longitude1 == null
                || latitude2 == null
                || longitude2 == null) {

            return -1.0;
        }

        final double earthRadiusKm = 6371.0;

        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);

        double deltaLat =
                Math.toRadians(latitude2 - latitude1);

        double deltaLon =
                Math.toRadians(longitude2 - longitude1);

        double a =
                Math.sin(deltaLat / 2)
                        * Math.sin(deltaLat / 2)
                        + Math.cos(lat1)
                        * Math.cos(lat2)
                        * Math.sin(deltaLon / 2)
                        * Math.sin(deltaLon / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return earthRadiusKm * c;
    }


    private String generateMatchReason(
            Surplus surplus,
            Need need,
            MatchScoreResponse score
    ) {

        StringBuilder reason =
                new StringBuilder();

        if (score.getCategoryScore() != null
                && score.getCategoryScore() == 30.0) {

            reason.append(
                    "Category matches. "
            );
        }

        if (score.getQuantityScore() != null
                && score.getQuantityScore() >= 15.0) {

            reason.append(
                    "Quantity is highly compatible. "
            );
        }

        if (score.getDistanceScore() != null
                && score.getDistanceScore() >= 15.0) {

            reason.append(
                    "Locations are nearby. "
            );
        }

        if (score.getUrgencyScore() != null
                && score.getUrgencyScore() >= 12.0) {

            reason.append(
                    "Need has high urgency. "
            );
        }

        if (reason.length() == 0) {
            reason.append(
                    "Potential match based on resource compatibility."
            );
        }

        return reason.toString().trim();
    }

    private LocalDateTime calculateExpiryTime(
            Surplus surplus
    ) {

        if (surplus.getAvailableUntil() != null) {
            return surplus.getAvailableUntil();
        }

        return LocalDateTime.now()
                .plusDays(7);
    }

    private Surplus findSurplus(UUID id) {

        return surplusRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Surplus not found with id: " + id
                        )
                );
    }

    private Need findNeed(UUID id) {

        return needRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Need not found with id: " + id
                        )
                );
    }

    private MatchResponse mapToResponse(
            Match match
    ) {

        return MatchResponse.builder()
                .id(match.getId())
                .surplusId(match.getSurplusId())
                .needId(match.getNeedId())
                .overallScore(match.getOverallScore())
                .categoryScore(match.getCategoryScore())
                .quantityScore(match.getQuantityScore())
                .distanceScore(match.getDistanceScore())
                .matchReason(match.getMatchReason())
                .urgencyScore(match.getUrgencyScore())
                .conditionScore(match.getConditionScore())
                .availabilityScore(
                        match.getAvailabilityScore()
                )
                .distanceKm(match.getDistanceKm())
                .status(match.getStatus())
                .createdAt(match.getCreatedAt())
                .expiresAt(match.getExpiresAt())
                .build();
    }

    private double round(double value) {

        return Math.round(value * 100.0)
                / 100.0;
    }
}
