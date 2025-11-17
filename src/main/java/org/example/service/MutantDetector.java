package org.example.service;

import org.springframework.stereotype.Service;

@Service
public class MutantDetector {
    public boolean isMutant(String[] dna) {
        final int N = dna.length;
        char[][] grid = new char[N][N];

        // 1. Llenar la matriz (Esto estaba perfecto)
        for (int i = 0; i < N; i++) {
            grid[i] = dna[i].toCharArray();
        }

        int contadorSecuencia = 0;

        // 2. Recorremos TODA la matriz (sin restar 4 aquí)
        for (int f = 0; f < N; f++) {
            for (int c = 0; c < N; c++) {

                char actual = grid[f][c];

                //  HORIZONTAL (→)
                // Solo buscamos si tenemos espacio a la derecha (c <= N-4)
                if (c <= N - 4) {
                    if (actual == grid[f][c+1] &&
                            actual == grid[f][c+2] &&
                            actual == grid[f][c+3]) {

                        contadorSecuencia++;
                        if (contadorSecuencia > 1) return true;
                    }
                }

                // VERTICAL (↓)
                // Solo buscamos si tenemos espacio abajo (f <= N-4)
                if (f <= N - 4) {
                    if (actual == grid[f+1][c] &&
                            actual == grid[f+2][c] &&
                            actual == grid[f+3][c]) {

                        contadorSecuencia++;
                        if (contadorSecuencia > 1) return true;
                    }
                }

                // DIAGONAL DESCENDENTE
                // Espacio a la derecha Y abajo
                if (f <= N - 4 && c <= N - 4) {
                    if (actual == grid[f+1][c+1] &&
                            actual == grid[f+2][c+2] &&
                            actual == grid[f+3][c+3]) {

                        contadorSecuencia++;
                        if (contadorSecuencia > 1) return true;
                    }
                }

                // DIAGONAL ASCENDENTE
                // Espacio a la derecha Y ARRIBA (f >= 3)
                if (f >= 3 && c <= N - 4) {
                    if (actual == grid[f-1][c+1] &&
                            actual == grid[f-2][c+2] &&
                            actual == grid[f-3][c+3]) {

                        contadorSecuencia++;
                        if (contadorSecuencia > 1) return true;
                    }
                }
            }
        }
        return false;
    }
}
