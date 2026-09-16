package com.example.surplus_management_system;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final MatchRepository matchRepository;
    private final SurplusRepository surplusRepository;
    private final NeedRepository needRepository;

    public RequestServiceImpl(
            RequestRepository requestRepository,
            MatchRepository matchRepository,
            SurplusRepository surplusRepository,
            NeedRepository needRepository
    ) {
        this.requestRepository = requestRepository;
        this.matchRepository = matchRepository;
        this.surplusRepository = surplusRepository;
        this.needRepository = needRepository;
    }

    @Override
    public RequestResponse createRequest(
            CreateRequestRequest request
    ) {

        Match match = findMatch(request.getMatchId());

        if (match.getStatus() != MatchStatus.ACTIVE) {
            throw new RuntimeException(
                    "Request can only be created for an active match"
            );
        }

        Surplus surplus =
                findSurplus(match.getSurplusId());

        Need need =
                findNeed(match.getNeedId());

        if (surplus.getStatus() != SurplusStatus.AVAILABLE) {
            throw new RuntimeException(
                    "Surplus is not available for request"
            );
        }

        BigDecimal quantityRequested =
                request.getQuantityRequested();

        if (quantityRequested == null
                || quantityRequested.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            throw new RuntimeException(
                    "Requested quantity must be greater than zero"
            );
        }

        if (surplus.getQuantity() == null
                || quantityRequested.compareTo(
                surplus.getQuantity()
        ) > 0) {

            throw new RuntimeException(
                    "Requested quantity exceeds available surplus"
            );
        }

        List<Request> existingRequests =
                requestRepository.findByMatchId(
                        match.getId()
                );

        boolean alreadyRequested =
                existingRequests.stream()
                        .anyMatch(existing ->
                                existing.getStatus()
                                        == RequestStatus.PENDING
                                        || existing.getStatus()
                                        == RequestStatus.ACCEPTED
                        );

        if (alreadyRequested) {
            throw new RuntimeException(
                    "An active request already exists for this match"
            );
        }

        Request newRequest = Request.builder()
                .matchId(match.getId())
                .surplusId(surplus.getId())
                .recipientOrganizationId(
                        need.getRecipientOrganizationId()
                )
                .providerOrganizationId(
                        surplus.getProviderOrganizationId()
                )
                .quantityRequested(
                        quantityRequested
                )
                .message(request.getMessage())
                .status(RequestStatus.PENDING)
                .build();

        Request savedRequest =
                requestRepository.save(newRequest);

        match.setStatus(MatchStatus.REQUESTED);
        matchRepository.save(match);

        return mapToResponse(savedRequest);
    }

    @Override
    public RequestResponse getRequest(UUID id) {

        Request request =
                findRequest(id);

        return mapToResponse(request);
    }

    @Override
    public List<RequestResponse> getAllRequests() {

        return requestRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestResponse> getRequestsByRecipient(
            UUID organizationId
    ) {

        return requestRepository
                .findByRecipientOrganizationId(
                        organizationId
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestResponse> getRequestsByProvider(
            UUID organizationId
    ) {

        return requestRepository
                .findByProviderOrganizationId(
                        organizationId
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestResponse> getRequestsByStatus(
            RequestStatus status
    ) {

        return requestRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RequestResponse updateStatus(
            UUID id,
            UpdateRequestStatusRequest request
    ) {

        Request existingRequest =
                findRequest(id);

        RequestStatus currentStatus =
                existingRequest.getStatus();

        RequestStatus newStatus =
                request.getStatus();

        validateStatusTransition(
                currentStatus,
                newStatus
        );

        if (newStatus == RequestStatus.ACCEPTED) {

            handleAcceptedRequest(
                    existingRequest
            );

        } else if (newStatus == RequestStatus.REJECTED) {

            handleRejectedRequest(
                    existingRequest
            );

        } else if (newStatus == RequestStatus.CANCELLED) {

            handleCancelledRequest(
                    existingRequest
            );

        } else if (newStatus == RequestStatus.COMPLETED) {

            handleCompletedRequest(
                    existingRequest
            );
        }

        existingRequest.setStatus(newStatus);

        Request savedRequest =
                requestRepository.save(
                        existingRequest
                );

        updateMatchStatus(
                existingRequest,
                newStatus
        );

        return mapToResponse(savedRequest);
    }

    @Override
    public void cancelRequest(UUID id) {

        Request request =
                findRequest(id);

        if (request.getStatus()
                != RequestStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending requests can be cancelled"
            );
        }

        request.setStatus(
                RequestStatus.CANCELLED
        );

        requestRepository.save(request);

        Match match =
                findMatch(request.getMatchId());

        match.setStatus(
                MatchStatus.ACTIVE
        );

        matchRepository.save(match);
    }

    private void handleAcceptedRequest(
            Request request
    ) {

        Surplus surplus =
                findSurplus(
                        request.getSurplusId()
                );

        if (surplus.getStatus()
                != SurplusStatus.AVAILABLE) {

            throw new RuntimeException(
                    "Surplus is no longer available"
            );
        }

        if (surplus.getQuantity() == null
                || request.getQuantityRequested()
                .compareTo(
                        surplus.getQuantity()
                ) > 0) {

            throw new RuntimeException(
                    "Requested quantity exceeds available surplus"
            );
        }

        request.setAcceptedAt(
                LocalDateTime.now()
        );

        surplus.setStatus(
                SurplusStatus.RESERVED
        );

        surplusRepository.save(surplus);
    }

    private void handleRejectedRequest(
            Request request
    ) {

        request.setRejectedAt(
                LocalDateTime.now()
        );
    }

    private void handleCancelledRequest(
            Request request
    ) {

        Match match =
                findMatch(request.getMatchId());

        match.setStatus(
                MatchStatus.ACTIVE
        );

        matchRepository.save(match);

        Surplus surplus =
                findSurplus(
                        request.getSurplusId()
                );

        if (surplus.getStatus()
                == SurplusStatus.RESERVED) {

            surplus.setStatus(
                    SurplusStatus.AVAILABLE
            );

            surplusRepository.save(surplus);
        }
    }

    private void handleCompletedRequest(
            Request request
    ) {

        request.setCompletedAt(
                LocalDateTime.now()
        );

        Surplus surplus =
                findSurplus(
                        request.getSurplusId()
                );

        BigDecimal remainingQuantity =
                surplus.getQuantity()
                        .subtract(
                                request.getQuantityRequested()
                        );

        if (remainingQuantity.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            surplus.setQuantity(
                    BigDecimal.ZERO
            );

            surplus.setStatus(
                    SurplusStatus.DELIVERED
            );

        } else {

            surplus.setQuantity(
                    remainingQuantity
            );

            surplus.setStatus(
                    SurplusStatus.AVAILABLE
            );
        }

        surplusRepository.save(surplus);
    }

    private void updateMatchStatus(
            Request request,
            RequestStatus requestStatus
    ) {

        Match match =
                findMatch(request.getMatchId());

        switch (requestStatus) {

            case PENDING:
                match.setStatus(
                        MatchStatus.REQUESTED
                );
                break;

            case ACCEPTED:
                match.setStatus(
                        MatchStatus.ACCEPTED
                );
                break;

            case REJECTED:
                match.setStatus(
                        MatchStatus.REJECTED
                );
                break;

            case CANCELLED:
                match.setStatus(
                        MatchStatus.ACTIVE
                );
                break;

            case COMPLETED:
                match.setStatus(
                        MatchStatus.ACCEPTED
                );
                break;
        }

        matchRepository.save(match);
    }

    private void validateStatusTransition(
            RequestStatus current,
            RequestStatus target
    ) {

        if (current == target) {
            throw new RuntimeException(
                    "Request is already in "
                            + target
                            + " status"
            );
        }

        boolean valid = switch (current) {

            case PENDING ->
                    target == RequestStatus.ACCEPTED
                            || target == RequestStatus.REJECTED
                            || target == RequestStatus.CANCELLED;

            case ACCEPTED ->
                    target == RequestStatus.COMPLETED
                            || target == RequestStatus.CANCELLED;

            case REJECTED,
                 CANCELLED,
                 COMPLETED ->
                    false;
        };

        if (!valid) {
            throw new RuntimeException(
                    "Invalid request status transition: "
                            + current
                            + " -> "
                            + target
            );
        }
    }

    private Request findRequest(UUID id) {

        return requestRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Request not found with id: "
                                        + id
                        )
                );
    }

    private Match findMatch(UUID id) {

        return matchRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Match not found with id: "
                                        + id
                        )
                );
    }

    private Surplus findSurplus(UUID id) {

        return surplusRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Surplus not found with id: "
                                        + id
                        )
                );
    }

    private Need findNeed(UUID id) {

        return needRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Need not found with id: "
                                        + id
                        )
                );
    }

    private RequestResponse mapToResponse(
            Request request
    ) {

        return RequestResponse.builder()
                .id(request.getId())
                .matchId(request.getMatchId())
                .surplusId(request.getSurplusId())
                .recipientOrganizationId(
                        request.getRecipientOrganizationId()
                )
                .providerOrganizationId(
                        request.getProviderOrganizationId()
                )
                .quantityRequested(
                        request.getQuantityRequested()
                )
                .message(request.getMessage())
                .status(request.getStatus())
                .requestedAt(request.getRequestedAt())
                .acceptedAt(request.getAcceptedAt())
                .rejectedAt(request.getRejectedAt())
                .completedAt(request.getCompletedAt())
                .build();
    }
}