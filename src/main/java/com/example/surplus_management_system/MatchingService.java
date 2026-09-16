package com.example.surplus_management_system;

import java.util.List;
import java.util.UUID;

public interface MatchingService {

    MatchScoreResponse calculateMatchScore(
            UUID surplusId,
            UUID needId
    );

    MatchResponse createMatch(
            UUID surplusId,
            UUID needId
    );

    List<MatchResponse> findBestMatchesForSurplus(
            UUID surplusId
    );

    List<MatchResponse> findBestMatchesForNeed(
            UUID needId
    );

    MatchResponse getMatch(UUID id);

    List<MatchResponse> getAllActiveMatches();

    void expireMatch(UUID id);
}
