package com.museando.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museando.model.Categoria;

public interface IntCategorias {

	public List<Categoria> obtenerCategorias();
	public void agregar(Categoria categoria);
	public Categoria buscarPorId(Integer IdCategoria);
	public void eliminar(Integer IdCategoria);
	public int totalCategorias();
	Page<Categoria> buscarTodas(Pageable page);
    boolean tieneMuseosAsociados(int IdCategoria);	
    boolean existePorNombre(String nombre);
    Categoria buscarPorNombre(String nombre);
	
}
