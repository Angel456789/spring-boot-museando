package com.museando.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Ubicaciones")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String calle;
    private String numeroExterior;
    private String colonia;
    private String codigoPostal;
    private String municipio;
    private String estado;
    

    @OneToMany(mappedBy = "ubicacion")
    private List<Museo> museos;

    
    
    public List<Museo> getMuseos() {
		return museos;
	}
	public void setMuseos(List<Museo> museos) {
		this.museos = museos;
	}
	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCalle() {
        return calle;
    }
    public void setCalle(String calle) {
        this.calle = calle;
    }
    public String getNumeroExterior() {
        return numeroExterior;
    }
    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }
    public String getColonia() {
        return colonia;
    }
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    public String getCodigoPostal() {
        return codigoPostal;
    }
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    public String getMunicipio() {
        return municipio;
    }
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
	@Override
	public String toString() {
		return "Ubicacion [id=" + id + ", calle=" + calle + ", numeroExterior=" + numeroExterior + ", colonia="
				+ colonia + ", codigoPostal=" + codigoPostal + ", municipio=" + municipio + ", estado=" + estado
				+ ", museos=" + museos + "]";
	}
    
    
} 
