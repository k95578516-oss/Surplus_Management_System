package com.example.surplus_management_system;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
@SecurityRequirement(name = "bearerAuth")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/score")
    public ResponseEntity<MatchScoreResponse> calculateMatchScore(
            @RequestParam UUID surplusId,
            @RequestParam UUID needId
    ) {
        return ResponseEntity.ok(
                matchingService.calculateMatchScore(
                        surplusId,
                        needId
                )
        );
    }

    @PostMapping
    public ResponseEntity<MatchResponse> createMatch(
            @RequestParam UUID surplusId,
            @RequestParam UUID needId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        matchingService.createMatch(
                                surplusId,
                                needId
                        )
                );
    }

    @GetMapping("/surplus/{surplusId}/best")
    public ResponseEntity<List<MatchResponse>> findBestMatchesForSurplus(
            @PathVariable UUID surplusId
    ) {
        return ResponseEntity.ok(
                matchingService.findBestMatchesForSurplus(surplusId)
        );
    }

    @GetMapping("/need/{needId}/best")
    public ResponseEntity<List<MatchResponse>> findBestMatchesForNeed(
            @PathVariable UUID needId
    ) {
        return ResponseEntity.ok(
                matchingService.findBestMatchesForNeed(needId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getMatch(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                matchingService.getMatch(id)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<MatchResponse>> getAllActiveMatches() {
        return ResponseEntity.ok(
                matchingService.getAllActiveMatches()
        );
    }

    @PatchMapping("/{id}/expire")
    public ResponseEntity<Void> expireMatch(
            @PathVariable UUID id
    ) {
        matchingService.expireMatch(id);

        return ResponseEntity.noContent().build();
    }
}

