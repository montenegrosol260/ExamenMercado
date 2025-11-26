package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGHT = 4;

    public boolean isMutant(String[] dna) {
        final int N = dna.length;
        char[][] grid = new char[N][N];

        // Llenar la matriz
        for (int i = 0; i < N; i++) {
            grid[i] = dna[i].toCharArray();
        }

        int contadorSecuencia = 0;

        // Recorremos TODA la matriz
        for (int f = 0; f < N; f++) {
            for (int c = 0; c < N; c++) {

                char actual = grid[f][c];

                // HORIZONTAL
                // Miramos atrás: IZQUIERDA (c-1)
                if (c <= N - SEQUENCE_LENGHT) {
                    if (c == 0 || grid[f][c - 1] != actual) {
                        if (checkHorizontal(grid, f, c)) {
                            contadorSecuencia++;
                            if (contadorSecuencia > 1) return true;
                        }
                    }
                }

                // VERTICAL
                // Miramos atrás: ARRIBA (f-1)
                if (f <= N - SEQUENCE_LENGHT) {
                    // CORREGIDO: Antes tenías 'c==0' y 'c-1' aquí por error
                    if (f == 0 || grid[f - 1][c] != actual) {
                        if (checkVertical(grid, f, c)) {
                            contadorSecuencia++;
                            if (contadorSecuencia > 1) return true;
                        }
                    }
                }

                // DIAGONAL DESCENDENTE \
                // Miramos atrás: ARRIBA-IZQUIERDA (f-1, c-1)
                if (f <= N - SEQUENCE_LENGHT && c <= N - SEQUENCE_LENGHT) {
                    if (f == 0 || c == 0 || grid[f - 1][c - 1] != actual) {
                        if (checkDiagonalDescending(grid, f, c)) {
                            contadorSecuencia++;
                            if (contadorSecuencia > 1) return true;
                        }
                    }
                }

                // DIAGONAL ASCENDENTE /
                // Miramos atrás: ABAJO-IZQUIERDA (f+1, c-1)
                if (f >= SEQUENCE_LENGHT - 1 && c <= N - SEQUENCE_LENGHT) {
                    if (f == N - 1 || c == 0 || grid[f + 1][c - 1] != actual) {
                        if (checkDiagonalAscending(grid, f, c)) {
                            contadorSecuencia++;
                            if (contadorSecuencia > 1) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // METODOS HELPER OPTIMIZADOS

    private boolean checkHorizontal(char[][] grid, int f, int c) {
        char start = grid[f][c];
        // Comparamos directamente las 3 posiciones siguientes a la derecha
        boolean match = grid[f][c + 1] == start &&
                         grid[f][c + 2] == start &&
                         grid[f][c + 3] == start;
        if (match) {
            // <--- 4. Usar el log
            log.info("Secuencia HORIZONTAL detectada en Fila {} Columna {}", f, c);
        }
        return match;
    }

    private boolean checkVertical(char[][] grid, int f, int c) {
        char start = grid[f][c];
        // Comparamos las 3 posiciones siguientes hacia abajo
        boolean match = grid[f + 1][c] == start &&
                        grid[f + 2][c] == start &&
                        grid[f + 3][c] == start;
        if (match) {
            // <--- 4. Usar el log
            log.info("Secuencia VERTICAL detectada en Fila {} Columna {}", f, c);
        }
        return match;
    }

    private boolean checkDiagonalDescending(char[][] grid, int f, int c) {
        char start = grid[f][c];
        // Comparamos hacia abajo y derecha (i+1, j+1)
        boolean match = grid[f + 1][c + 1] == start &&
                        grid[f + 2][c + 2] == start &&
                        grid[f + 3][c + 3] == start;
        if (match) {
            // <--- 4. Usar el log
            log.info("Secuencia DIAGONAL DESCENDENTE detectada en Fila {} Columna {}", f, c);
        }
        return match;
    }

    private boolean checkDiagonalAscending(char[][] grid, int f, int c) {
        char start = grid[f][c];
        // Comparamos hacia arriba y derecha (i-1, j+1)
        boolean match = grid[f - 1][c + 1] == start &&
                        grid[f - 2][c + 2] == start &&
                        grid[f - 3][c + 3] == start;
        if (match) {
            // <--- 4. Usar el log
            log.info("Secuencia DIAGONAL ASCENDENTE detectada en Fila {} Columna {}", f, c);
        }
        return match;
    }
}