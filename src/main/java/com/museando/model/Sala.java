package com.museando.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name="Salas")
public class Sala {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
    private String nombre;
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private TipoSala tipo;
    
	 @ManyToOne
	    @JoinColumn(name = "idMuseo")
	 private Museo museo;

	 
	 
	 public TipoSala getTipo() {
		return tipo;
	}

	 public void setTipo(TipoSala tipo) {
		 this.tipo = tipo;
	 }

	 public Integer getId() {
		 return id;
	 }

	 public void setId(Integer id) {
		 this.id = id;
	 }

	 public String getNombre() {
		 return nombre;
	 }

	 public void setNombre(String nombre) {
		 this.nombre = nombre;
	 }

	 public String getDescripcion() {
		 return descripcion;
	 }

	 public void setDescripcion(String descripcion) {
		 this.descripcion = descripcion;
	 }

	 public Museo getMuseo() {
		 return museo;
	 }

	 public void setMuseo(Museo museo) {
		 this.museo = museo;
	 }

	 @Override
	 public String toString() {
		return "Sala [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", tipo=" + tipo + ", museo="
				+ museo + "]";
	 }

}
