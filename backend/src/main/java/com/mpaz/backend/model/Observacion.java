package com.mpaz.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "observaciones")
public class Observacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estudiante;
    private String docente;
    @Column(length = 1000)
    private String comentario;
    private String fecha;

    public Observacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEstudiante() { return estudiante; }
    public void setEstudiante(String estudiante) { this.estudiante = estudiante; }
    public String getDocente() { return docente; }
    public void setDocente(String docente) { this.docente = docente; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
