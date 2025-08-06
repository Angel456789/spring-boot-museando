package com.museando.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museando.model.Obra;


public interface IntObras {
	
	public List<Obra> obtenerObras();
    List<Obra> buscarPorSala(Integer idSala);
    public void agregar(Obra obra);
    public Obra buscarPorId(Integer idObra);
    public void eliminar(Integer idObra);
    public int totalObras();
    
    //nueva logica
  	Page<Obra> buscarTodas(Pageable page);

}
