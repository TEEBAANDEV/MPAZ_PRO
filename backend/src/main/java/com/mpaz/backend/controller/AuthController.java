package com.mpaz.backend.controller;

import com.mpaz.backend.model.Usuario;
import com.mpaz.backend.repository.UsuarioRepository;
import com.mpaz.backend.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        return usuarioRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .<ResponseEntity<?>>map(u -> {
                    String token = jwtService.generateToken(u.getUsername(), u.getRol());
                    return ResponseEntity.ok(Map.of(
                            "token", token,
                            "username", u.getUsername(),
                            "nombreCompleto", u.getNombreCompleto(),
                            "rol", u.getRol()
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciales inválidas")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El usuario ya existe"));
        }
        Usuario saved = usuarioRepository.save(usuario);
        String token = jwtService.generateToken(saved.getUsername(), saved.getRol());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "token", token,
                "username", saved.getUsername(),
                "nombreCompleto", saved.getNombreCompleto(),
                "rol", saved.getRol()
        ));
    }
}
