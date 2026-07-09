package com.sanosysalvos.ms_registro_mascotas.service;

import com.sanosysalvos.ms_registro_mascotas.model.Mascota;
import com.sanosysalvos.ms_registro_mascotas.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceImplTest {

    @Mock
    private MascotaRepository repository;

    @InjectMocks
    private MascotaServiceImpl service;

    private Mascota bobby;

    @BeforeEach
    void setUp() {
        bobby = new Mascota(1L, "Bobby", "Perro", "Mestizo", 3, "Puerto Montt", "Muy juguetón", "Disponible");
    }

    @Test
    void listarTodas_debeRetornarTodasLasMascotas() {
        when(repository.findAll()).thenReturn(List.of(bobby));

        List<Mascota> resultado = service.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Bobby");
        verify(repository, times(1)).findAll();
    }

    @Test
    void listarPorEstado_debeFiltrarPorEstado() {
        when(repository.findByEstado("Disponible")).thenReturn(List.of(bobby));

        List<Mascota> resultado = service.listarPorEstado("Disponible");

        assertThat(resultado).extracting(Mascota::getEstado).containsOnly("Disponible");
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarLaMascota() {
        when(repository.findById(1L)).thenReturn(Optional.of(bobby));

        Optional<Mascota> resultado = service.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Mascota> resultado = service.buscarPorId(99L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void registrar_debeGuardarYRetornarLaMascota() {
        when(repository.save(bobby)).thenReturn(bobby);

        Mascota resultado = service.registrar(bobby);

        assertThat(resultado.getNombre()).isEqualTo("Bobby");
        verify(repository, times(1)).save(bobby);
    }
}
