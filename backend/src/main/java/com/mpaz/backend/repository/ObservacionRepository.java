package com.mpaz.backend.repository;

import com.mpaz.backend.model.Observacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservacionRepository extends JpaRepository<Observacion, Long> {
}
