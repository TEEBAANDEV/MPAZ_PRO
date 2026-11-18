package com.mpaz.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notas")
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estudiante;
    private String asignatura;
    private String tituloEvaluacion;
    private double calificacion;
    private String retroalimentacion;
    private String fecha;

    public Nota() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEstudiante() { return estudiante; }
    public void setEstudiante(String estudiante) { this.estudiante = estudiante; }
    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }
    public String getTituloEvaluacion() { return tituloEvaluacion; }
    public void setTituloEvaluacion(String tituloEvaluacion) { this.tituloEvaluacion = tituloEvaluacion; }
    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }
    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
