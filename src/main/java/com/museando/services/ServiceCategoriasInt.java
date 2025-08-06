package com.museando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.museando.model.Categoria;
import com.museando.repository.CategoriasRepository;

@Service
public class ServiceCategoriasInt implements IntCategorias {
	
	@Autowired
	private CategoriasRepository repoCategorias;

	@Override
	public List<Categoria> obtenerCategorias() {
		return repoCategorias.findAll();
	}

	@Override
	public void agregar(Categoria categoria) {
		repoCategorias.save(categoria);
	}

	@Override
	public Categoria buscarPorId(Integer IdCategoria) {
		Categoria categoria = null;
		Optional<Categoria>optional =repoCategorias.findById(IdCategoria);
		if(optional.isPresent()) {
			categoria = optional.get();
		}
		return categoria;
	}

	@Override
	public void eliminar(Integer IdCategoria) {
		repoCategorias.deleteById(IdCategoria);
	}

	@Override
	public int totalCategorias() {
		return obtenerCategorias().size();
	}

	@Override
	public Page<Categoria> buscarTodas(Pageable page) {
		return repoCategorias.findAll(page);
	}

	@Override
	public boolean tieneMuseosAsociados(int IdCategoria) {
	    Categoria categoria = buscarPorId(IdCategoria);
	    if (categoria != null && categoria.getMuseos() != null) {
	        return !categoria.getMuseos().isEmpty();
	    }
	    return false;
	}

	@Override
	public boolean existePorNombre(String nombre) {
	    return repoCategorias.findByNombre(nombre) != null;
	}

	@Override
	public Categoria buscarPorNombre(String nombre) {
	    return repoCategorias.findByNombre(nombre);
	}



}
