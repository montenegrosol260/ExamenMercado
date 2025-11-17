package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MutantController {
    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    public ResponseEntity<String> checkMutant(@Valid @RequestBody DnaRequest dnaRequest) {
        //le pasamos el array de String que vino del JSON
        boolean isMutant = mutantService.analyzeDna(dnaRequest.getDna());

        if (isMutant) {
            return ResponseEntity.ok("Mutant Detected");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Human Detected");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats(){
        return ResponseEntity.ok(statsService.getStats());
    }

}
