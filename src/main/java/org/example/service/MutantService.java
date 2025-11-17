package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // Inyecta automáticamente el Repositorio y el Detector
public class MutantService {

    // Las herramientas que el gerente necesita:
    private final DnaRecordRepository dnaRecordRepository;
    private final MutantDetector mutantDetector;

    /**
     * Este es el método principal que llamará el Controller.
     */
    public boolean analyzeDna(String[] dna) {

        // Generar la "Huella Digital" (Hash) del ADN
        // Convertimos el array en un solo texto para usarlo como ID único.
        String dnaHash = String.join("", dna);

        // Preguntar a la Memoria (Base de Datos)
        // ¿Ya analizamos este ADN antes?
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);

        if (existingRecord.isPresent()) {
            // Devolvemos el resultado guardado y nos ahorramos el cálculo.
            return existingRecord.get().isMutant();
        }

        // Preguntar al Experto (Si no estaba en memoria)
        // Como es nuevo, llamamos al algoritmo detector.
        boolean isMutant = mutantDetector.isMutant(dna);

        // Guardar en Memoria
        // Guardamos el resultado para que la próxima vez sea rápido.
        DnaRecord newRecord = DnaRecord.builder()
                .dnaHash(dnaHash)
                .isMutant(isMutant)
                .build();

        dnaRecordRepository.save(newRecord);

        // Devolver el resultado final
        return isMutant;
    }
}