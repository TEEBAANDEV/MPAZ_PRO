package com.mpaz.backend.controller;

import com.mpaz.backend.model.Asignacion;
import com.mpaz.backend.repository.AsignacionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "*")
public class AsignacionController {

    private final AsignacionRepository repository;

    public AsignacionController(AsignacionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Asignacion> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Asignacion crear(@RequestBody Asignacion asignacion) {
        return repository.save(asignacion);
    }
}
