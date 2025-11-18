package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

//trabajamos con dobles de riesgo, para no traer el repositorio y el detector que son mas costosos
@ExtendWith(MockitoExtension.class)
public class MutantServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @Mock
    private MutantDetector mutantDetector;

    @InjectMocks //servicio real
    private MutantService mutantService;
    // TEST 1, YA EXISTE EN LA BD
    @Test
    void testAnalyzeDnaFoundInCache(){
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};

        //simulamos que la BD encuentra un registro que dice que es mutante
        DnaRecord existingRecord = DnaRecord.builder().isMutant(true).build();
        // indico que cuando el servicio me pide buscar un dna especifico yo le devuelvo este
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingRecord));

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);

        verify(mutantDetector, never()).isMutant(any());

        verify(dnaRecordRepository, never()).save(any());

    }

    // CACHE MISS - ES MUTANTE
    @Test
    void testAnalyzeDnaNotFoundInCacheIsMutant() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        //simulamos que la bd no encuentra nada
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);

        verify(mutantDetector, times(1)).isMutant(dna);

        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    void testAnalyzeDnaNotFoundInCacheIsHuman() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        //simulamos que la bd no encuentra nada
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(false);

        boolean result = mutantService.analyzeDna(dna);

        assertFalse(result);

        verify(mutantDetector, times(1)).isMutant(dna);

        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    // --- TEST 4: VERIFICAR CÁLCULO DE HASH (Integridad) ---
    @Test
    void testAnalyzeDnaCalculatesCorrectHash() {
        // GIVEN
        String[] dna = {"A"};
        // "A" en SHA-256 es: 559aead08264d5795d3909718cdd05abd49572e84fe55590eef31a88a08fdffd
        String expectedHash = "559aead08264d5795d3909718cdd05abd49572e84fe55590eef31a88a08fdffd";

        // WHEN
        mutantService.analyzeDna(dna);

        // THEN
        // Verificamos que el repositorio haya sido llamado CON ESE HASH EXACTO
        // Si el cálculo interno estuviera mal, este test fallaría.
        verify(dnaRecordRepository).findByDnaHash(eq(expectedHash));
    }

    // --- TEST 5: VERIFICAR DATOS AL GUARDAR (Integridad) ---
    @Test
    void testAnalyzeDnaSavesCorrectData() {
        // GIVEN
        String[] dna = {"A"};
        String expectedHash = "559aead08264d5795d3909718cdd05abd49572e84fe55590eef31a88a08fdffd";

        // Simulamos que no existe y que es mutante
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        // WHEN
        mutantService.analyzeDna(dna);

        // THEN
        // Usamos un "Captor" para atrapar el objeto que se le pasó al método save()
        ArgumentCaptor<DnaRecord> recordCaptor = ArgumentCaptor.forClass(DnaRecord.class);
        verify(dnaRecordRepository).save(recordCaptor.capture());

        // Ahora inspeccionamos el objeto atrapado
        DnaRecord savedRecord = recordCaptor.getValue();

        assertEquals(expectedHash, savedRecord.getDnaHash()); // ¿El hash es correcto?
        assertTrue(savedRecord.isMutant()); // ¿El resultado es true?
    }
}
