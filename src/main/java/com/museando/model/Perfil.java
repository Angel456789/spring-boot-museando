package com.museando.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Perfiles")
public class Perfil {
	
	@Id
	private Integer id;
	private String perfil;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	@Override
	public String toString() {
		return "Perfil [id=" + id + ", perfil=" + perfil + "]";
	}
	
}
