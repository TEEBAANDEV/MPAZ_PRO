package com.mpaz.backend.controller;

import com.mpaz.backend.model.ObjetivoAprendizaje;
import com.mpaz.backend.repository.ObjetivoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objetivos")
@CrossOrigin(origins = "*")
public class ObjetivoController {

    private final ObjetivoRepository repository;

    public ObjetivoController(ObjetivoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ObjetivoAprendizaje> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ObjetivoAprendizaje crear(@RequestBody ObjetivoAprendizaje objetivo) {
        return repository.save(objetivo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjetivoAprendizaje> actualizar(@PathVariable Long id, @RequestBody ObjetivoAprendizaje datos) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCodigo(datos.getCodigo());
                    existing.setTitulo(datos.getTitulo());
                    existing.setDescripcion(datos.getDescripcion());
                    existing.setAsignatura(datos.getAsignatura());
                    existing.setEstado(datos.getEstado());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
