package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.dto.HomeDashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Backend For Frontend (BFF): en vez de que el frontend haga varias llamadas
 * (una por microservicio) y las combine en el navegador, el BFF hace esa
 * orquestación en el servidor y expone un único endpoint ya listo para pintar
 * la pantalla de Inicio.
 */
@RestController
@RequestMapping("/bff")
@CrossOrigin(origins = "*")
@Tag(name = "BFF - Inicio", description = "Vistas agregadas pensadas para el frontend")
public class HomeController {

    private final RestTemplate restTemplate;

    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Operation(summary = "Dashboard de inicio", description =
            "Devuelve las mascotas disponibles (ms-registro-mascotas) para la home del frontend.")
    @GetMapping("/inicio")
    public ResponseEntity<HomeDashboardResponse> obtenerDashboardInicio() {
        List<Map<String, Object>> mascotas = obtenerListaSeguro(
                "http://ms-registro-mascotas/api/mascotas/estado/Disponible");

        // ms-organizacion (campañas) se eliminó del proyecto para reducir el
        // alcance a 5 microservicios funcionales; se deja la lista vacía en
        // vez de romper el contrato de HomeDashboardResponse que consume el frontend.
        HomeDashboardResponse dashboard = new HomeDashboardResponse(
                mascotas.stream().limit(6).toList(),
                Collections.emptyList(),
                mascotas.size()
        );

        return ResponseEntity.ok(dashboard);
    }

    /**
     * Llama a un microservicio downstream y, si falla o no responde
     * (servicio caído, timeout, etc.), degrada de forma segura devolviendo
     * una lista vacía en lugar de romper toda la respuesta del BFF.
     */
    private List<Map<String, Object>> obtenerListaSeguro(String url) {
        try {
            List<Map<String, Object>> respuesta = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();
            return respuesta != null ? respuesta : Collections.emptyList();
        } catch (RestClientException ex) {
            return Collections.emptyList();
        }
    }
}
