package com.example.surplus_management_system;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final RequestRepository requestRepository;
    private final SurplusRepository surplusRepository;
    private final NeedRepository needRepository;
    private final MatchRepository matchRepository;

    public DeliveryServiceImpl(
            DeliveryRepository deliveryRepository,
            RequestRepository requestRepository,
            SurplusRepository surplusRepository,
            NeedRepository needRepository,
            MatchRepository matchRepository
    ) {
        this.deliveryRepository = deliveryRepository;
        this.requestRepository = requestRepository;
        this.surplusRepository = surplusRepository;
        this.needRepository = needRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public DeliveryResponse createDelivery(CreateDeliveryRequest request) {

        Request existingRequest = requestRepository.findById(request.getRequestId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Request not found")
                );

        if (existingRequest.getStatus() != RequestStatus.ACCEPTED) {
            throw new IllegalStateException(
                    "Delivery can only be created for an accepted request"
            );
        }

        if (deliveryRepository.findByRequestId(request.getRequestId()).isPresent()) {
            throw new IllegalStateException(
                    "Delivery already exists for this request"
            );
        }

        Surplus surplus = surplusRepository.findById(existingRequest.getSurplusId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Surplus not found")
                );

        Need need = findNeedFromMatch(existingRequest.getMatchId());

        Double pickupLatitude = surplus.getLatitude();
        Double pickupLongitude = surplus.getLongitude();

        Double deliveryLatitude = need.getLatitude();
        Double deliveryLongitude = need.getLongitude();

        Double distanceKm = calculateDistance(
                pickupLatitude,
                pickupLongitude,
                deliveryLatitude,
                deliveryLongitude
        );

        Integer estimatedMinutes = calculateEstimatedMinutes(distanceKm);

        Delivery delivery = Delivery.builder()
                .requestId(existingRequest.getId())
                .pickupLatitude(pickupLatitude)
                .pickupLongitude(pickupLongitude)
                .deliveryLatitude(deliveryLatitude)
                .deliveryLongitude(deliveryLongitude)
                .distanceKm(distanceKm)
                .estimatedDeliveryMinutes(estimatedMinutes)
                .expectedPickupAt(request.getExpectedPickupAt())
                .expectedDeliveryAt(
                        calculateExpectedDeliveryTime(
                                request.getExpectedPickupAt(),
                                estimatedMinutes
                        )
                )
                .status(DeliveryStatus.REQUESTED)
                .deliveryMethod(request.getDeliveryMethod())
                .build();

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return mapToResponse(savedDelivery);
    }

    @Override
    public DeliveryResponse getDelivery(UUID id) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Delivery not found")
                );

        return mapToResponse(delivery);
    }

    @Override
    public DeliveryResponse getDeliveryByRequest(UUID requestId) {

        Delivery delivery = deliveryRepository.findByRequestId(requestId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Delivery not found for request"
                        )
                );

        return mapToResponse(delivery);
    }

    @Override
    public List<DeliveryResponse> getAllDeliveries() {

        return deliveryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<DeliveryResponse> getDeliveriesByStatus(
            DeliveryStatus status
    ) {

        return deliveryRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DeliveryResponse updateStatus(
            UUID id,
            UpdateDeliveryStatusRequest request
    ) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Delivery not found")
                );

        DeliveryStatus currentStatus = delivery.getStatus();
        DeliveryStatus newStatus = request.getStatus();

        validateStatusTransition(currentStatus, newStatus);

        delivery.setStatus(newStatus);

        LocalDateTime now = LocalDateTime.now();

        if (newStatus == DeliveryStatus.IN_TRANSIT
                && delivery.getActualPickupAt() == null) {

            delivery.setActualPickupAt(now);
        }

        if (newStatus == DeliveryStatus.DELIVERED) {

            if (delivery.getActualPickupAt() == null) {
                delivery.setActualPickupAt(now);
            }

            delivery.setActualDeliveryAt(now);

            completeRequestAndSurplus(delivery);
        }

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return mapToResponse(savedDelivery);
    }

    @Override
    public DeliveryResponse scheduleDelivery(
            UUID id,
            ScheduleDeliveryRequest request
    ) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Delivery not found")
                );

        if (delivery.getStatus() != DeliveryStatus.ACCEPTED) {
            throw new IllegalStateException(
                    "Only accepted deliveries can be scheduled"
            );
        }

        if (request.getExpectedPickupAt() == null
                || request.getExpectedDeliveryAt() == null) {

            throw new IllegalArgumentException(
                    "Expected pickup and delivery times are required"
            );
        }

        if (request.getExpectedDeliveryAt()
                .isBefore(request.getExpectedPickupAt())) {

            throw new IllegalArgumentException(
                    "Expected delivery time cannot be before pickup time"
            );
        }

        delivery.setExpectedPickupAt(
                request.getExpectedPickupAt()
        );

        delivery.setExpectedDeliveryAt(
                request.getExpectedDeliveryAt()
        );

        delivery.setStatus(
                DeliveryStatus.PICKUP_SCHEDULED
        );

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return mapToResponse(savedDelivery);
    }

    @Override
    public DeliveryETAResponse getDeliveryETA(UUID id) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Delivery not found")
                );

        Integer estimatedMinutes =
                delivery.getEstimatedDeliveryMinutes();

        LocalDateTime estimatedArrival = null;

        if (estimatedMinutes != null) {
            estimatedArrival = LocalDateTime.now()
                    .plusMinutes(estimatedMinutes);
        }

        boolean delayed = false;

        if (delivery.getExpectedDeliveryAt() != null
                && estimatedArrival != null) {

            delayed = estimatedArrival.isAfter(
                    delivery.getExpectedDeliveryAt()
            );
        }

        return DeliveryETAResponse.builder()
                .distanceKm(delivery.getDistanceKm())
                .estimatedDeliveryMinutes(estimatedMinutes)
                .estimatedArrival(
                        estimatedArrival != null
                                ? estimatedArrival.toString()
                                : null
                )
                .delayed(delayed)
                .build();
    }

    private void validateStatusTransition(
            DeliveryStatus currentStatus,
            DeliveryStatus newStatus
    ) {

        if (currentStatus == newStatus) {
            throw new IllegalStateException(
                    "Delivery is already in this status"
            );
        }

        boolean valid = switch (currentStatus) {

            case REQUESTED ->
                    newStatus == DeliveryStatus.ACCEPTED
                            || newStatus == DeliveryStatus.CANCELLED;

            case ACCEPTED ->
                    newStatus == DeliveryStatus.PICKUP_SCHEDULED
                            || newStatus == DeliveryStatus.IN_TRANSIT
                            || newStatus == DeliveryStatus.CANCELLED;

            case PICKUP_SCHEDULED ->
                    newStatus == DeliveryStatus.READY_FOR_PICKUP
                            || newStatus == DeliveryStatus.IN_TRANSIT
                            || newStatus == DeliveryStatus.CANCELLED;

            case READY_FOR_PICKUP ->
                    newStatus == DeliveryStatus.IN_TRANSIT
                            || newStatus == DeliveryStatus.CANCELLED;

            case IN_TRANSIT ->
                    newStatus == DeliveryStatus.DELIVERED
                            || newStatus == DeliveryStatus.FAILED;

            case DELIVERED, FAILED, CANCELLED ->
                    false;
        };

        if (!valid) {
            throw new IllegalStateException(
                    "Invalid delivery status transition: "
                            + currentStatus
                            + " -> "
                            + newStatus
            );
        }
    }

    private void completeRequestAndSurplus(
            Delivery delivery
    ) {

        Request request = requestRepository.findById(
                        delivery.getRequestId()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Request not found")
                );

        if (request.getStatus() != RequestStatus.ACCEPTED) {
            return;
        }

        BigDecimal quantityDelivered =
                delivery.getQuantityDelivered();

        if (quantityDelivered == null) {
            quantityDelivered = request.getQuantityRequested();
            delivery.setQuantityDelivered(quantityDelivered);
        }

        if (quantityDelivered == null
                || quantityDelivered.signum() <= 0) {

            throw new IllegalStateException(
                    "Delivered quantity must be greater than zero"
            );
        }

        Surplus surplus = surplusRepository.findById(
                        request.getSurplusId()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Surplus not found")
                );

        if (surplus.getQuantity() == null) {
            throw new IllegalStateException(
                    "Surplus quantity is not available"
            );
        }

        BigDecimal remainingQuantity =
                surplus.getQuantity()
                        .subtract(quantityDelivered);

        if (remainingQuantity.signum() < 0) {
            throw new IllegalStateException(
                    "Delivered quantity exceeds available surplus"
            );
        }

        surplus.setQuantity(remainingQuantity);

        if (remainingQuantity.signum() == 0) {
            surplus.setStatus(SurplusStatus.DELIVERED);
        } else {
            surplus.setStatus(SurplusStatus.AVAILABLE);
        }

        request.setStatus(RequestStatus.COMPLETED);
        request.setCompletedAt(LocalDateTime.now());

        requestRepository.save(request);
        surplusRepository.save(surplus);
    }

    private Need findNeedFromMatch(UUID matchId) {

        Match match = findMatch(matchId);

        return needRepository.findById(match.getNeedId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Need not found for match: " + matchId
                        )
                );
    }

    private Match findMatch(UUID matchId) {

        if (matchId == null) {
            throw new IllegalArgumentException(
                    "Request does not have a match"
            );
        }

        return matchRepository.findById(matchId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Match not found: " + matchId
                        )
                );
    }

    private Double calculateDistance(
            Double pickupLatitude,
            Double pickupLongitude,
            Double deliveryLatitude,
            Double deliveryLongitude
    ) {

        if (pickupLatitude == null
                || pickupLongitude == null
                || deliveryLatitude == null
                || deliveryLongitude == null) {

            return null;
        }

        double earthRadiusKm = 6371.0;

        double latitudeDifference = Math.toRadians(
                deliveryLatitude - pickupLatitude
        );

        double longitudeDifference = Math.toRadians(
                deliveryLongitude - pickupLongitude
        );

        double a =
                Math.sin(latitudeDifference / 2)
                        * Math.sin(latitudeDifference / 2)
                        + Math.cos(Math.toRadians(pickupLatitude))
                        * Math.cos(Math.toRadians(deliveryLatitude))
                        * Math.sin(longitudeDifference / 2)
                        * Math.sin(longitudeDifference / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return earthRadiusKm * c;
    }

    private Integer calculateEstimatedMinutes(
            Double distanceKm
    ) {

        if (distanceKm == null) {
            return null;
        }

        double averageSpeedKmPerHour = 40.0;

        double hours =
                distanceKm / averageSpeedKmPerHour;

        return Math.max(
                1,
                (int) Math.ceil(hours * 60)
        );
    }

    private LocalDateTime calculateExpectedDeliveryTime(
            LocalDateTime expectedPickupAt,
            Integer estimatedMinutes
    ) {

        if (expectedPickupAt == null
                || estimatedMinutes == null) {

            return null;
        }

        return expectedPickupAt.plusMinutes(
                estimatedMinutes
        );
    }

    private DeliveryResponse mapToResponse(
            Delivery delivery
    ) {

        return DeliveryResponse.builder()
                .id(delivery.getId())
                .requestId(delivery.getRequestId())
                .pickupLatitude(delivery.getPickupLatitude())
                .pickupLongitude(delivery.getPickupLongitude())
                .deliveryLatitude(delivery.getDeliveryLatitude())
                .deliveryLongitude(delivery.getDeliveryLongitude())
                .distanceKm(delivery.getDistanceKm())
                .estimatedDeliveryMinutes(
                        delivery.getEstimatedDeliveryMinutes()
                )
                .expectedPickupAt(
                        delivery.getExpectedPickupAt()
                )
                .lastLocationUpdateAt(
                        delivery.getLastLocationUpdateAt()
                )
                .expectedDeliveryAt(
                        delivery.getExpectedDeliveryAt()
                )
                .actualPickupAt(
                        delivery.getActualPickupAt()
                )
                .actualDeliveryAt(
                        delivery.getActualDeliveryAt()
                )
                .status(delivery.getStatus())
                .deliveryMethod(delivery.getDeliveryMethod())
                .deliveryPartner(delivery.getDeliveryPartner())
                .proofUrl(delivery.getProofUrl())
                .quantityDelivered(delivery.getQuantityDelivered())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
    }
}

