package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;



//Esta clase implementa la lógica de negocio para la anotación @ValidDnaSequence
//Spring la llamará automáticamente cada vez que vea esta notación en un bean
//Esta lógica es para la etiqueta <ValidDnaSequence> y valida un tipo <String[]>
public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {
    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context){
       //revisamos que el array no sea nulo ni esté vacío (el array de array)
        if(dna == null || dna.length == 0){
            return false;
        }

        if (dna.length < 4) {
            return false;
        }
        final int N = dna.length;
        //recorremos cada fila
        char[][] grid = new char[N][N]; // optimización
        for (int i = 0; i < N; i++) {
            if (dna[i] == null || dna[i].length() != N) return false;
            grid[i] = dna[i].toCharArray(); // Convertimos una sola vez
        }

        // Loop rápido
        for (int f = 0; f < N; f++) {
            for (int c = 0; c < N; c++) {
                char ch = grid[f][c]; // Acceso súper rápido
                if (ch != 'A' && ch != 'T' && ch != 'C' && ch != 'G') {
                    return false;
                }
            }
        }

        return true;
    }
}
