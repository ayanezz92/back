package com.sanosysalvos.ms_registro_mascotas.service;

import com.sanosysalvos.ms_registro_mascotas.model.Mascota;
import com.sanosysalvos.ms_registro_mascotas.repository.MascotaRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository repository;

    public MascotaServiceImpl(MascotaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = "mascotas", key = "'todas'")
    public List<Mascota> listarTodas() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "mascotasPorEstado", key = "#estado")
    public List<Mascota> listarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    @Override
    @Cacheable(value = "mascota", key = "#id")
    public Optional<Mascota> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    // Al registrar una mascota nueva, invalidamos los listados cacheados
    // para que no queden desactualizados.
    @CacheEvict(value = {"mascotas", "mascotasPorEstado"}, allEntries = true)
    public Mascota registrar(Mascota mascota) {
        return repository.save(mascota);
    }

    @Override
    @CacheEvict(value = {"mascotas", "mascotasPorEstado", "mascota"}, allEntries = true)
    public Optional<Mascota> actualizar(Long id, Mascota datos) {
        return repository.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setEspecie(datos.getEspecie());
            existente.setRaza(datos.getRaza());
            existente.setEdad(datos.getEdad());
            existente.setZona(datos.getZona());
            existente.setDescripcion(datos.getDescripcion());
            existente.setEstado(datos.getEstado());
            return repository.save(existente);
        });
    }

    @Override
    @CacheEvict(value = {"mascotas", "mascotasPorEstado", "mascota"}, allEntries = true)
    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
