package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        String dnaHash = calculateHash(dna);

        // Preguntar a la Memoria (Base de Datos)
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);

        if (existingRecord.isPresent()) {
            // Devolvemos el resultado guardado y nos ahorramos el cálculo.
            return existingRecord.get().isMutant();
        }

        // Preguntar al Experto si no estaba en memoria
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
    private String calculateHash(String[] dna) {
        try {
            // Unimos el array en un solo String ("ATCG...")
            String dnaString = String.join("", dna);

            // Usamos el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dnaString.getBytes());

            // Convertimos los bytes a texto Hexadecimal legible
            BigInteger number = new BigInteger(1, hashBytes);
            StringBuilder hexString = new StringBuilder(number.toString(16));

            // Rellenamos con ceros a la izquierda si faltan (para que sean 64 caracteres)
            while (hexString.length() < 64) {
                hexString.insert(0, '0');
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error interno al calcular hash", e);
        }
    }
    @Transactional
    public boolean deleteDna(String hash){
        Optional<DnaRecord> record = dnaRecordRepository.findByDnaHash(hash);

        if (record.isPresent()) {
            dnaRecordRepository.deleteByDnaHash(hash);
            return true; // Borrado con éxito
        }
        return false; // No existía
    }
}