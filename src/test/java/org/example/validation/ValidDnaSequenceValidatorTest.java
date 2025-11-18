package org.example.validation;

import org.example.service.MutantDetector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ValidDnaSequenceValidatorTest {
    private final MutantDetector detector = new MutantDetector();
    private final ValidDnaSequenceValidator validator = new ValidDnaSequenceValidator();

    @Test
    void testNotMutantWithEmptyDna() {
        // Caso: Array vacío
        String[] dna = {};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testNotMutantWithNonSquaredDna() {
        // Caso: NxM (4 filas, 3 columnas)
        String[] dna = {
                "ATG",
                "CAG",
                "TTA",
                "AGA"
        };
        // Probamos al VALIDATOR, no al detector
        assertFalse(validator.isValid(dna, null));
    }

    @Test
    void testNotMutantWithInvalidCharacters() {
        // Caso: Contiene 'X' y números
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTXT", // X inválida
                "1234"  // Números inválidos
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testNotMutantWithNullRow() {
        // Caso: Una fila es null
        String[] dna = {
                "ATGC",
                null,
                "ATGC",
                "ATGC"
        };
        assertFalse(validator.isValid(dna, null));
    }

    @Test
    void testNotMutantWithTooSmallDna() {
        // Caso: Matriz 3x3 (Menor a 4)
        // Aunque sea cuadrada y tenga letras válidas, no sirve.
        String[] dna = {
                "ATG",
                "CAG",
                "TTA"
        };
        assertFalse(detector.isMutant(dna));
    }

}
