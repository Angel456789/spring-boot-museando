package com.museando.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Voluntariados")
public class Voluntariado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate fecha;
    private String archivo; // nombre o ruta del archivo
    
    @Column(columnDefinition = "TEXT")
    private String comentarios;
    // Relación con Museo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMuseo")
    private Museo museo;

    // Relación con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    
    @Enumerated(EnumType.STRING)  // Guarda el nombre del Enum en la DB
    private EstadoVoluntariado estado = EstadoVoluntariado.PENDIENTE;
    

    public Voluntariado() {
        this.fecha = LocalDate.now(); // fecha actual por defecto
    }

    public EstadoVoluntariado getEstado() {
        return estado;
    }

    public void setEstado(EstadoVoluntariado estado) {
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Museo getMuseo() {
        return museo;
    }

    public void setMuseo(Museo museo) {
        this.museo = museo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

	@Override
	public String toString() {
		return "Voluntariado [id=" + id + ", fecha=" + fecha + ", archivo=" + archivo + ", comentarios=" + comentarios
				+ ", museo=" + museo + ", usuario=" + usuario + "]";
	}
    
    
}
