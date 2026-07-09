package com.sanosysalvos.adopciones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "adopciones")
public class Adopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guardamos los nombres descriptivos directamente para la grilla rápida del Staff
    private String mascota;
    private String adoptante;

    // IDs de referencia cruzada arquitectural hacia los otros microservicios
    // 🚨 CORREGIDO: la tabla real (creada por postgres-init/init.sh) tiene la columna
    // "mascota_id", pero Hibernate mapeaba por defecto el campo "idMascota" a la
    // columna "id_mascota" (que no existe). Con ddl-auto=update esto generaba una
    // columna nueva y huérfana en vez de usar la que ya tenía datos, así que las
    // consultas y el guardado no eran consistentes con el resto del sistema.
    @Column(name = "mascota_id")
    private Long idMascota;
    private Long idAdoptante;

    private String estado; // "PENDIENTE", "APROBADA", "RECHAZADA"

    // Constructor Vacío
    public Adopcion() {}

    public Adopcion(Long id, String mascota, String adoptante, Long idMascota, Long idAdoptante, String estado) {
        this.id = id;
        this.mascota = mascota;
        this.adoptante = adoptante;
        this.idMascota = idMascota;
        this.idAdoptante = idAdoptante;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMascota() { return mascota; }
    public void setMascota(String mascota) { this.mascota = mascota; }
    public String getAdoptante() { return adoptante; }
    public void setAdoptante(String adoptante) { this.adoptante = adoptante; }
    public Long getIdMascota() { return idMascota; }
    public void setIdMascota(Long idMascota) { this.idMascota = idMascota; }
    public Long getIdAdoptante() { return idAdoptante; }
    public void setIdAdoptante(Long idAdoptante) { this.idAdoptante = idAdoptante; }
    public String getEstado() { return estado; }
    public void setExtendedEstado(String estado) { this.estado = estado; }
    public void setEstado(String estado) { this.estado = estado; }
}