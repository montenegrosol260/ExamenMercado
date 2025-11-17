package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private long count_Mutant_Dna;
    private long count_Human_Dna;
    private double ratio;
}
