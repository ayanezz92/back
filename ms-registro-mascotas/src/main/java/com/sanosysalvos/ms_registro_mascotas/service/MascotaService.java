package com.sanosysalvos.ms_registro_mascotas.service;

import com.sanosysalvos.ms_registro_mascotas.model.Mascota;

import java.util.List;
import java.util.Optional;

public interface MascotaService {
    List<Mascota> listarTodas();
    List<Mascota> listarPorEstado(String estado);
    Optional<Mascota> buscarPorId(Long id);
    Mascota registrar(Mascota mascota);
    // 🚨 AGREGADO: el panel de administración (Usuarios.jsx) permite editar y eliminar
    // filas, pero el backend nunca tuvo estas operaciones implementadas.
    Optional<Mascota> actualizar(Long id, Mascota datos);
    boolean eliminar(Long id);
}
