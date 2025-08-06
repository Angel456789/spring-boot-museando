package com.museando.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museando.model.Museo;

public interface IntMuseos {
	public List<Museo> obtenerMuseos();
    public void agregar(Museo museo);
    public Museo buscarPorId(Integer idMuseo);
    public void eliminar(Integer idMuseo);
    public int totalMuseos();

  //nueva logica: consultar por estados y estatus
  	public List<Museo> buscarPorEstatusYDestacado();
  	
  	 boolean existePorNombre(String nombre);
     Museo buscarPorNombre(String nombre);
  	
  //nueva logica
  	Page<Museo> buscarTodas(Pageable page);
  	
  	 // Métodos nuevos para filtrado
    Page<Museo> buscarPorNombre(String nombre, Pageable pageable);

    Page<Museo> buscarPorCategoria(Integer categoriaId, Pageable pageable);

    Page<Museo> buscarPorNombreYcategoria(String nombre, Integer categoriaId, Pageable pageable);
   

}
