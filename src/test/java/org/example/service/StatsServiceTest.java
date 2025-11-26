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
    private StatsService statsService;

    @Test
    void testGetStats_normalProcess(){
        // Given
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // When
        // Nota: Esto funciona si aplicaste la sobrecarga en StatsService
        StatsResponse response = statsService.getStats();

        // Then
        assertEquals(40, response.getCountMutantDna()); // Corregido a CamelCase
        assertEquals(100, response.getCountHumanDna()); // Corregido a CamelCase
        assertEquals(0.4, response.getRatio());
    }

    @Test
    void testGetStats_ZeroHumans() {
        // Given: 40 mutantes, 0 humanos
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // When
        StatsResponse response = statsService.getStats();

        // Then
        assertEquals(40, response.getCountMutantDna());
        assertEquals(0, response.getCountHumanDna());

        // CORREGIDO: La lógica del servicio devuelve 0.0 si hay 0 humanos para evitar error
        assertEquals(0.0, response.getRatio());
    }

    @Test
    void testGetStats_ZeroMutants() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        StatsResponse response = statsService.getStats();

        assertEquals(0, response.getCountMutantDna());
        assertEquals(50, response.getCountHumanDna());
        assertEquals(0.0, response.getRatio());
    }

    @Test
    void testGetStats_EmptyDatabase() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse response = statsService.getStats();

        assertEquals(0, response.getCountMutantDna());
        assertEquals(0, response.getCountHumanDna());
        assertEquals(0.0, response.getRatio());
    }

    @Test
    @DisplayName("Debe calcular ratio con decimales correctamente")
    void testGetStatsWithDecimalRatio() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(1L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(3L);

        StatsResponse response = statsService.getStats();

        assertEquals(1, response.getCountMutantDna());
        assertEquals(3, response.getCountHumanDna());
        assertEquals(0.333, response.getRatio(), 0.01);  // Usamos delta para comparar dobles
    }

    @Test
    @DisplayName("Debe retornar ratio 1.0 cuando hay igual cantidad")
    void testGetStatsWithEqualCounts() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(50L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        StatsResponse stats = statsService.getStats();

        assertEquals(50, stats.getCountMutantDna());
        assertEquals(50, stats.getCountHumanDna());
        assertEquals(1.0, stats.getRatio());
    }
}