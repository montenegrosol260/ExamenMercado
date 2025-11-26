package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    @JsonProperty("count_mutant_dna") // Nombre para el JSON
    private long countMutantDna;      // Nombre para Java

    @JsonProperty("count_human_dna")
    private long countHumanDna;

    private double ratio;
}
