package com.sanosysalvos.bff.dto;

import java.util.List;
import java.util.Map;

/**
 * Vista agregada pensada específicamente para lo que la pantalla de Inicio del
 * frontend necesita: evita que el cliente tenga que hacer 2-3 llamadas por
 * separado y combinar la respuesta en el navegador.
 */
public record HomeDashboardResponse(
        List<Map<String, Object>> mascotasDestacadas,
        List<Map<String, Object>> campaniasActivas,
        long totalMascotasDisponibles
) {
}
