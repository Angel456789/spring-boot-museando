package com.museando.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table (name="Museos")
public class Museo {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String descripcion;
	
	  @NotNull(message = "{NotNull.museo.fechaFundacion}")
	    @PastOrPresent(message = "La fecha debe ser hoy o en el pasado")
	private LocalDate fechaFundacion;
	private String telefono;
	private String correo;
	private String sitioWeb;
	private String costos;
	private String horario;
	
	@Enumerated(EnumType.STRING)
    private EstatusMuseo estatus;
	
	private Integer destacado;
	private String imagen = "logo.png";
	
	 @ManyToOne
	    @JoinColumn(name = "idCategoria")
	    private Categoria categoria;

	 @ManyToOne
	    @JoinColumn(name = "idUbicacion")
	    private Ubicacion ubicacion;
	 
	 @OneToMany(mappedBy = "museo")
	 	private List<ImagenMuseo> imagenes;


	

	public EstatusMuseo getEstatus() {
		return estatus;
	}
	 public void setEstatus(EstatusMuseo estatus) {
		 this.estatus = estatus;
	 }
	 public List<ImagenMuseo> getImagenes() {
		 return imagenes;
	 }
	 public void setImagenes(List<ImagenMuseo> imagenes) {
		 this.imagenes = imagenes;
	 }
	public String getImagen() {
		return imagen;
	}
	 public void setImagen(String imagen) {
		 this.imagen = imagen;
	 }
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Ubicacion getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}
	
	public Integer getDestacado() {
		return destacado;
	}
	public void setDestacado(Integer destacado) {
		this.destacado = destacado;
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
	public LocalDate getFechaFundacion() {
		return fechaFundacion;
	}
	public void setFechaFundacion(LocalDate fechaFundacion) {
		this.fechaFundacion = fechaFundacion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getSitioWeb() {
		return sitioWeb;
	}
	public void setSitioWeb(String sitioWeb) {
		this.sitioWeb = sitioWeb;
	}
	public String getCostos() {
		return costos;
	}
	public void setCostos(String costos) {
		this.costos = costos;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	@Override
	public String toString() {
	    return "Museo [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion
	            + ", fechaFundacion=" + fechaFundacion + ", telefono=" + telefono
	            + ", correo=" + correo + ", sitioWeb=" + sitioWeb + ", costos=" + costos
	            + ", horario=" + horario + ", estatus=" + estatus + ", destacado=" + destacado
	            + ", imagen=" + imagen + "]";
	}

	
	
	
	

}
