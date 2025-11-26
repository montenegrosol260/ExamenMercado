package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatsService {
    // Las herramientas que el gerente necesita:
    private final DnaRecordRepository dnaRecordRepository;

    public StatsResponse getStats() {
        // Llama al método principal pasando nulos (sin filtro de fecha)
        return getStats(null, null);
    }

    // Ahora recibe fechas (pueden ser null)
    public StatsResponse getStats(LocalDate from, LocalDate to) {

        // Definimos valores por defecto si vienen null
        // Si 'from' es null, usamos el inicio de los tiempos (o hace 100 años)
        LocalDateTime start = (from != null) ? from.atStartOfDay() : LocalDateTime.MIN;

        // Si 'to' es null, usamos el momento actual
        LocalDateTime end = (to != null) ? to.atTime(23, 59, 59) : LocalDateTime.now();

        // Usamos el nuevo método del repo
        long countMutant = dnaRecordRepository.countByIsMutantAndCreatedAtBetween(true, start, end);
        long countHuman = dnaRecordRepository.countByIsMutantAndCreatedAtBetween(false, start, end);

        double ratio = (countHuman > 0) ? (double) countMutant / countHuman : 0;

        return new StatsResponse(countMutant, countHuman, ratio);
    }

}
