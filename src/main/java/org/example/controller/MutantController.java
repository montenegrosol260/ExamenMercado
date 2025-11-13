package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MutantController {
    private final MutantService mutantService;

    public StatsResponse<String> verificarMutante(){
        boolean esMutante = mutantService.isMutant(reques.getDna());
    }
}
