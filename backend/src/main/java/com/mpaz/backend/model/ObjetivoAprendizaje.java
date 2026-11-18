package com.mpaz.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "objetivos")
public class ObjetivoAprendizaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String titulo;
    @Column(length = 1000)
    private String descripcion;
    private String asignatura;
    private String estado;

    public ObjetivoAprendizaje() {}

    public ObjetivoAprendizaje(Long id, String codigo, String titulo, String descripcion, String asignatura, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.asignatura = asignatura;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
