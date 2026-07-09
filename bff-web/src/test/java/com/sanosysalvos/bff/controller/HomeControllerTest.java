package com.sanosysalvos.bff.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HomeController homeController;

    // ms-organizacion (campañas) se eliminó del proyecto al reducirlo a 5
    // microservicios funcionales; el dashboard ahora solo agrega mascotas
    // disponibles y siempre devuelve campaniasActivas vacío.
    @Test
    void obtenerDashboardInicio_devuelveMascotasDisponibles() {
        List<Map<String, Object>> mascotas = List.of(Map.of("id", 1, "nombre", "Bobby"));

        when(restTemplate.exchange(
                eq("http://ms-registro-mascotas/api/mascotas/estado/Disponible"),
                eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mascotas));

        var response = homeController.obtenerDashboardInicio();

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().totalMascotasDisponibles()).isEqualTo(1);
        assertThat(response.getBody().mascotasDestacadas()).hasSize(1);
        assertThat(response.getBody().campaniasActivas()).isEmpty();
    }

    @Test
    void obtenerDashboardInicio_degradaSuaveSiMascotasFalla() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RestClientException("Servicio no disponible"));

        var response = homeController.obtenerDashboardInicio();

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().mascotasDestacadas()).isEmpty();
        assertThat(response.getBody().campaniasActivas()).isEmpty();
        assertThat(response.getBody().totalMascotasDisponibles()).isZero();
    }
}
