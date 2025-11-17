package org.example.service;

import org.springframework.stereotype.Service;

@Service
public class MutantDetector {
    //definimos una constante que podría variar con el tiempo
    private static final int SEQUENCE_LENGHT = 4;

    public boolean isMutant(String[] dna) {
        final int N = dna.length;
        char[][] grid = new char[N][N];

        // Llenar la matriz
        for (int i = 0; i < N; i++) {
            grid[i] = dna[i].toCharArray();
        }

        int contadorSecuencia = 0;

        // Recorremos TODA la matriz (sin restar 4 aquí)
        for (int f = 0; f < N; f++) {
            for (int c = 0; c < N; c++) {

                char actual = grid[f][c];

                //  HORIZONTAL
                // Solo buscamos si tenemos espacio a la derecha (c <= N-4)
                if (c <= N - SEQUENCE_LENGHT) {
                    if (checkHorizontal(grid, f, c)) {
                        contadorSecuencia++;
                        if (contadorSecuencia > 1) return true;
                    }
                }

                // VERTICAL
                // Solo buscamos si tenemos espacio abajo
                if (f <= N - SEQUENCE_LENGHT) {
                    if (checkVertical(grid, f, c)) {
                        contadorSecuencia++;
                        if (contadorSecuencia > 1) return true;
                    }
                }

                // DIAGONAL DESCENDENTE
                // Espacio a la derecha Y abajo
                if (f <= N - SEQUENCE_LENGHT && c <= N - SEQUENCE_LENGHT) {
                    if (checkDiagonalDescending(grid, f, c)) {
                        contadorSecuencia++;
                        if (contadorSecuencia > 1) return true;
                    }
                }

                // DIAGONAL ASCENDENTE
                // Espacio a la derecha Y ARRIBA (f >= 3)
                if (f >= SEQUENCE_LENGHT - 1 && c <= N - SEQUENCE_LENGHT) {
                    if (checkDiagonalAscending(grid, f, c)) {
                        contadorSecuencia++;
                        if (contadorSecuencia > 1) return true;
                    }
                }
            }
        }
        return false;
    }
    //Desacoplamos la lógica de las 4 maneras de obtener el
    private boolean checkHorizontal(char[][] grid, int f, int c){
        char start = grid[f][c];
        for(int i = 1; i < SEQUENCE_LENGHT; i++){
            if(grid[f][c + i] != start) return false;
        }

        return true;
    }
    private boolean checkVertical(char[][] grid, int f, int c){
        char start = grid[f][c];
        for(int i = 1; i < SEQUENCE_LENGHT; i++){
            if(grid[f + i][c] != start) return false;
        }

        return true;
    }
    private boolean checkDiagonalAscending(char[][] grid, int f, int c){
        char start = grid[f][c];
        for(int i = 1; i < SEQUENCE_LENGHT; i++){
            if(grid[f - i][c + i] != start) return false;
        }

        return true;
    }
    private boolean checkDiagonalDescending(char[][] grid, int f, int c){
        char start = grid[f][c];
        for(int i = 1; i < SEQUENCE_LENGHT; i++){
            if(grid[f + i][c + i] != start) return false;
        }

        return true;
    }
}
