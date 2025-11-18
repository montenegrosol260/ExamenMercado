package org.example.service;

import org.example.validation.ValidDnaSequenceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();

    @Test
    void testMutantWithHorizontalAndDiagonalSequences() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // ← Horizontal: CCCC
                "TCACTG"
        };

        assertTrue(detector.isMutant(dna));  // ← Debe retornar true
    }

    @Test
    void testMutantWithVerticalSequences() {
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "ATAAGG",
                "GCCTAT",
                "TCACTG"
        };

        assertTrue(detector.isMutant(dna));  // ← Debe retornar true
    }

    @Test
    void testMutantWithMultipleHorizontalSequences() {
        String[] dna = {
                "ATGCGA",
                "ATTTTC",
                "GTATGT",
                "TTAAGG",
                "GCCCCT",
                "TCACTG"
        };

        assertTrue(detector.isMutant(dna));  // ← Debe retornar true
    }

    @Test
    void testMutantWithBothDiagonals() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TCATGT",
                "AGCAGG",
                "CCCCTA",
                "TCACTG"
        };

        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantWithLargeDna() {
        // Matriz 10x10 llena de As
        String[] dna = {
                "AAAAAAAAAA", "AAAAAAAAAA", "AAAAAAAAAA", "AAAAAAAAAA", "AAAAAAAAAA",
                "AAAAAAAAAA", "AAAAAAAAAA", "AAAAAAAAAA", "AAAAAAAAAA", "AAAAAAAAAA"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantAllSameCharacter() {
        String[] dna = {
                "AAAA",
                "AAAA",
                "AAAA",
                "AAAA",
        };

        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testNotMutantWithOnlyOneSequence() {
        String[] dna = {
                "AAAA",
                "CGGT",
                "CAGT",
                "CAGT"
        };
        // Debe ser falso porque la regla dice "MÁS DE UNA" secuencia
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testNotMutantWithNoSequences() {
        String[] dna = {
                "AAAG",
                "CAGT",
                "CTGT",
                "CAGT"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testNotMutantSmallDna() {
        String[] dna = {
                "AATA",
                "CAGT",
                "CAGT",
                "CAGT"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testMutantDiagonalInCorner() {
        // Matriz 5x5 para probar límites
        String[] dna = {
                "AAAAA",
                "CTGGG",
                "GGTGG",
                "GGGTG",
                "CCCCT" // 1. Horizontal de Cs (Fila 4)
                // 2. Diagonal de Ts (Empieza en 0,1 y termina en 4,4)
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testNotMutantWithSequenceLongerThanFour() {
        String[] dna = {
                "AAAAA", // 5 As seguidas. Esto cuenta como 1 secuencia.
                "CCGTC",
                "TTATG",
                "AGAAG",
                "CCCTA"
        };
        assertFalse(detector.isMutant(dna));
    }


}
