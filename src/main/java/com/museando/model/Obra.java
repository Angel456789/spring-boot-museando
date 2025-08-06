package com.museando.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name="Obras")

public class Obra {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;

    private String titulo;
    private String autor;
    
    @NotNull(message = "{NotNull.museo.fechaFundacion}")
    @PastOrPresent(message = "La fecha debe ser hoy o en el pasado")
    private LocalDate año;
    
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private TipoObra tipo;
    
	private String imagen;

    @ManyToOne
    @JoinColumn(name = "idSala")
    private Sala sala;
    
    

	public TipoObra getTipo() {
		return tipo;
	}

	public void setTipo(TipoObra tipo) {
		this.tipo = tipo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public LocalDate getAño() {
		return año;
	}

	public void setAño(LocalDate año) {
		this.año = año;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	@Override
	public String toString() {
		return "Obra [id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", año=" + año + ", descripcion="
				+ descripcion + ", tipo=" + tipo + ", imagen=" + imagen + ", sala=" + sala + "]";
	}

	
    

}
