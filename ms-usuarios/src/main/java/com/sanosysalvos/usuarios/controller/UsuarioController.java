package com.sanosysalvos.usuarios.controller;

import com.sanosysalvos.usuarios.model.Usuario;
import com.sanosysalvos.usuarios.repository.UsuarioRepository;
import com.sanosysalvos.usuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;
    private final UsuarioService usuarioService; // 👈 Inyectamos el servicio transaccional

    // Constructor actualizado con ambas dependencias
    public UsuarioController(UsuarioRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listar")
    public List<Usuario> obtenerTodos() {
        return repository.findAll();
    }

    @PostMapping("/registrar")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        // 🚨 CAMBIO CLAVE: Ahora pasa por el servicio para asegurar el guardado físico en la BD
        return usuarioService.registrarUsuario(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginForm) {
        // 🚨 CORREGIDO: si el email venía con espacios/mayúsculas distintas a como se
        // guardó (registrarUsuario lo normaliza a trim().toLowerCase()) la búsqueda no
        // encontraba al usuario. Además, si loginForm.getContrasena() venía null,
        // el .equals() de la versión anterior lanzaba NullPointerException -> 500.
        String email = loginForm.getEmail() == null ? "" : loginForm.getEmail().trim().toLowerCase();
        Optional<Usuario> userOpt = repository.findByEmail(email);

        boolean credencialesValidas = userOpt.isPresent()
                && userOpt.get().getContrasena() != null
                && userOpt.get().getContrasena().equals(loginForm.getContrasena());

        if (credencialesValidas) {
            return ResponseEntity.ok(userOpt.get());
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas o usuario inexistente.");
    }
}