package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {
    // Las herramientas que el gerente necesita:
    private final DnaRecordRepository dnaRecordRepository;

    public StatsResponse getStats(){
        long countMutant = dnaRecordRepository.countByIsMutant(true);
        long countHuman = dnaRecordRepository.countByIsMutant(false);

        double ratio = 0.0;
        if(countHuman > 0){
            ratio = (double) countMutant / countHuman;
        }
        // Si no hay humanos, retorna el número de mutantes
        if (countHuman == 0) {
            return new StatsResponse(countMutant, countHuman, countMutant);
        }

        return new StatsResponse(countMutant, countHuman, ratio);
    }

}
