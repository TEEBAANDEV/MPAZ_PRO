package com.mpaz.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asignaciones")
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long objetivoId;
    private String curso;
    private int limiteIntentos;
    private String fechaInicio;
    private String fechaFin;

    public Asignacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getObjetivoId() { return objetivoId; }
    public void setObjetivoId(Long objetivoId) { this.objetivoId = objetivoId; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public int getLimiteIntentos() { return limiteIntentos; }
    public void setLimiteIntentos(int limiteIntentos) { this.limiteIntentos = limiteIntentos; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
}
