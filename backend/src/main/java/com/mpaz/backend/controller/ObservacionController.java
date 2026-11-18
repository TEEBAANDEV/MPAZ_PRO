package com.mpaz.backend.controller;

import com.mpaz.backend.model.Observacion;
import com.mpaz.backend.repository.ObservacionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observaciones")
@CrossOrigin(origins = "*")
public class ObservacionController {

    private final ObservacionRepository repository;

    public ObservacionController(ObservacionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Observacion> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Observacion crear(@RequestBody Observacion observacion) {
        return repository.save(observacion);
    }
}
