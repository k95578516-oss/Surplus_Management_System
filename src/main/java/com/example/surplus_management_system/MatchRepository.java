package com.example.surplus_management_system;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findBySurplusIdOrderByOverallScoreDesc(
            UUID surplusId
    );

    List<Match> findByNeedIdOrderByOverallScoreDesc(
            UUID needId
    );

    List<Match> findByStatus(
            MatchStatus status
    );

    List<Match> findBySurplusIdAndStatusOrderByOverallScoreDesc(
            UUID surplusId,
            MatchStatus status
    );

    List<Match> findByNeedIdAndStatusOrderByOverallScoreDesc(
            UUID needId,
            MatchStatus status
    );

    Optional<Match> findBySurplusIdAndNeedIdAndStatus(
            UUID surplusId,
            UUID needId,
            MatchStatus status
    );
}

