package org.example.controller;

import org.example.service.MutantService;
import org.example.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class) //levantq un "servidor" para probar las peticiones HTTP
public class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    // TEST MUTANTE DETECTADO
    @Test
    void testProcessMutantDna_Returns200() throws Exception {
        when (mutantService.analyzeDna(any())).thenReturn(true);

        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dna\":[\"AAAA\",\"CCCC\",\"TCAG\",\"GGTC\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mutant Detected"));
    }

    // TEST HUMANO DETECTADO
    @Test
    void testProcessHumanDna_Returns403() throws Exception {
        when (mutantService.analyzeDna(any())).thenReturn(false);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dna\":[\"TAAA\",\"CCCA\",\"TCAG\",\"GGTC\"]}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Human Detected"));
    }
    // TEST ARRAY VACIO
    @Test
    void testInvalidDna_Empty_Return400() throws Exception {

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dna\":[]}"))
                .andExpect(status().isBadRequest());
    }

    // TEST CARACTERES INVÁLIDOS
    @Test
    void testInvalidCharacter_Return400() throws Exception {

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dna\":[\\\"AAAX\\\",\\\"CCCC\\\",\\\"TCAG\\\",\\\"GGTC\\\"]}"))
                .andExpect(status().isBadRequest());
    }
    // TEST VALIDAR NULL
    @Test
    void testInvalidDna_Null_Returns400() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Falta el campo "dna"
                .andExpect(status().isBadRequest());
    }
    // TEST MATRIZ MUY PEQUEÑA
    @Test
    void testInvalidDna_TooSmall_Returns400() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dna\":[\"AAA\",\"CCC\",\"TTT\"]}")) // 3x3 es menor a 4
                .andExpect(status().isBadRequest());
    }
    // TEST  JSON MAL FORMADO
    @Test
    void testMalformedJson_Returns400() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dna\":[\"AAAA\"")) // Falta cerrar corchetes y llaves
                .andExpect(status().isBadRequest());
    }
}
