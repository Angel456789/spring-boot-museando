package com.museando.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museando.model.Sala;

public interface IntSalas {

		public List<Sala> obtenerSalas();
		List<Sala> buscarPorMuseo(Integer idMuseo);
	    public void agregar(Sala sala);
	    public Sala buscarPorId(Integer idSala);
	    public void eliminar(Integer idSala);
	    public int totalSalas();


	  //nueva logica
	  	Page<Sala> buscarTodas(Pageable page);
}
