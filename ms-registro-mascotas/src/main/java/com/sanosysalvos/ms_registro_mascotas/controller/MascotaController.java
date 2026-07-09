package com.sanosysalvos.ms_registro_mascotas.controller;

import com.sanosysalvos.ms_registro_mascotas.model.Mascota;
import com.sanosysalvos.ms_registro_mascotas.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@Tag(name = "Mascotas", description = "Registro y consulta de mascotas disponibles/adoptadas/buscadas")
public class MascotaController {

    private final MascotaService service;

    public MascotaController(MascotaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todas las mascotas registradas (respuesta cacheada en Redis)")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/listar")
    public List<Mascota> listarTodas() {
        return service.listarTodas();
    }

    // 🚨 AGREGADO: el panel de administración (Usuarios.jsx) consulta GET /api/mascotas
    // directamente (sin "/listar"), lo que antes devolvía 404. En vez de forzar a tocar
    // el frontend en cada llamada, se agrega este alias equivalente a "/listar".
    @Operation(summary = "Alias de /listar usado por el panel administrativo")
    @GetMapping("")
    public List<Mascota> listarTodasAlias() {
        return service.listarTodas();
    }

    @Operation(summary = "Listar mascotas filtradas por estado", description = "Estados válidos: Disponible, Adoptada, Buscada")
    @GetMapping("/estado/{estado}")
    public List<Mascota> listarPorEstado(@PathVariable String estado) {
        return service.listarPorEstado(estado);
    }

    @Operation(summary = "Obtener una mascota por su id")
    @ApiResponse(responseCode = "200", description = "Mascota encontrada")
    @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Registrar una nueva mascota", description = "Invalida la caché de los listados")
    @PostMapping("/registrar")
    public Mascota registrarMascota(@RequestBody Mascota mascota) {
        return service.registrar(mascota);
    }

    // 🚨 AGREGADO: el panel de administración llama a POST /api/mascotas (sin "/registrar")
    // para crear, lo que causaba el 404 visto en pantalla ("Error en la transacción de
    // guardado."). Se agrega este alias equivalente a "/registrar".
    @Operation(summary = "Alias de /registrar usado por el panel administrativo")
    @PostMapping("")
    public Mascota registrarMascotaAlias(@RequestBody Mascota mascota) {
        return service.registrar(mascota);
    }

    // 🚨 AGREGADO: no existía ningún endpoint de edición. El panel administrativo llama
    // a PUT /api/mascotas/{id} para guardar cambios de una fila y siempre fallaba.
    @Operation(summary = "Actualizar una mascota existente")
    @ApiResponse(responseCode = "200", description = "Actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizarMascota(@PathVariable Long id, @RequestBody Mascota datos) {
        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 🚨 AGREGADO: no existía ningún endpoint de eliminación. El panel administrativo
    // llama a DELETE /api/mascotas/{id} y siempre fallaba.
    @Operation(summary = "Eliminar una mascota por id")
    @ApiResponse(responseCode = "204", description = "Eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {
        boolean eliminado = service.eliminar(id);
        return eliminado
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
