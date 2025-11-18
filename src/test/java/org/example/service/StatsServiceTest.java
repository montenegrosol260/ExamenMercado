package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {
    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private  StatsService statsService;

    @Test
    void testGetStats_normalProcess(){
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        StatsResponse response = statsService.getStats();

        assertEquals(40, response.getCount_Mutant_Dna());
        assertEquals(100, response.getCount_Human_Dna());
        assertEquals(0.4, response.getRatio());
    }

    @Test
    void testGetStats_ZeroHumans() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse response = statsService.getStats();

        assertEquals(40, response.getCount_Mutant_Dna());
        assertEquals(0, response.getCount_Human_Dna());
        assertEquals(40.0, response.getRatio());
    }

    @Test
    void testGetStats_ZeroMutants() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        StatsResponse response = statsService.getStats();

        assertEquals(0, response.getCount_Mutant_Dna());
        assertEquals(50, response.getCount_Human_Dna());
        assertEquals(0.0, response.getRatio());
    }

    @Test
    void testGetStats_EmptyDatabase() {
        // GIVEN: 0 de todo
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // WHEN
        StatsResponse response = statsService.getStats();

        // THEN
        assertEquals(0, response.getCount_Mutant_Dna());
        assertEquals(0, response.getCount_Human_Dna());
        assertEquals(0.0, response.getRatio());
    }

    @Test
    @DisplayName("Debe calcular ratio con decimales correctamente")
    void testGetStatsWithDecimalRatio() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(1L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(3L);

        StatsResponse response = statsService.getStats();

        assertEquals(1, response.getCount_Mutant_Dna());
        assertEquals(3, response.getCount_Human_Dna());
        assertEquals(0.333, response.getRatio(), 0.001);  // 1/3 = 0.333...
    }

    @Test
    @DisplayName("Debe retornar ratio 1.0 cuando hay igual cantidad")
    void testGetStatsWithEqualCounts() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(50L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        StatsResponse stats = statsService.getStats();

        assertEquals(50, stats.getCount_Mutant_Dna());
        assertEquals(50, stats.getCount_Human_Dna());
        assertEquals(1.0, stats.getRatio(), 0.001);  // 50/50 = 1.0
    }
}
