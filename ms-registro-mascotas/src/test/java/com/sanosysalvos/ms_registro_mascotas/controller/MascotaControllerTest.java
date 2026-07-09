package com.sanosysalvos.ms_registro_mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.ms_registro_mascotas.model.Mascota;
import com.sanosysalvos.ms_registro_mascotas.service.MascotaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MascotaController.class)
class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MascotaService service;

    @Test
    void listarTodas_debeRetornar200YJson() throws Exception {
        Mascota bobby = new Mascota(1L, "Bobby", "Perro", "Mestizo", 3, "Puerto Montt", "Juguetón", "Disponible");
        when(service.listarTodas()).thenReturn(List.of(bobby));

        mockMvc.perform(get("/api/mascotas/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Bobby"));
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/mascotas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registrarMascota_debeRetornar200ConLaMascotaCreada() throws Exception {
        Mascota nueva = new Mascota(null, "Kira", "Gato", "Siames", 1, "Puerto Varas", "Tranquila", "Disponible");
        Mascota guardada = new Mascota(2L, "Kira", "Gato", "Siames", 1, "Puerto Varas", "Tranquila", "Disponible");
        when(service.registrar(org.mockito.ArgumentMatchers.any(Mascota.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/mascotas/registrar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }
}
