package com.museando.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museando.model.ImagenMuseo;

public interface IntImagenesMuseo {	    
	    
	    public List<ImagenMuseo> obtenerTodas();
	    public List<ImagenMuseo> obtenerPorMuseo(Integer idMuseo);
	    public void guardar(ImagenMuseo imagen);
	    public ImagenMuseo buscarPorId(Integer idImagenMuseo);
	    public void eliminar(Integer idImagenMuseo);
	    public int totalImagenes();
	    public Page<ImagenMuseo> buscarTodas(Pageable page);
}