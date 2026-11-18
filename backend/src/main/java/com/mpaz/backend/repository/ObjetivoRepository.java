package com.mpaz.backend.repository;

import com.mpaz.backend.model.ObjetivoAprendizaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjetivoRepository extends JpaRepository<ObjetivoAprendizaje, Long> {
}
