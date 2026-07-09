package com.sanosysalvos.historial.repository;

import com.sanosysalvos.historial.model.FichaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<FichaMedica, Long> {
    List<FichaMedica> findByMascotaIgnoreCase(String mascota);
}