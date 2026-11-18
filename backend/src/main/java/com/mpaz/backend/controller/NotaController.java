package com.mpaz.backend.controller;

import com.mpaz.backend.model.Nota;
import com.mpaz.backend.repository.NotaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notas")
@CrossOrigin(origins = "*")
public class NotaController {

    private final NotaRepository repository;

    public NotaController(NotaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Nota> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Nota crear(@RequestBody Nota nota) {
        return repository.save(nota);
    }
}
