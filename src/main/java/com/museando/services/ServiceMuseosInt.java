package com.museando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.museando.model.EstatusMuseo;
import com.museando.model.Museo;
import com.museando.repository.MuseosRepository;

@Service
public class ServiceMuseosInt implements IntMuseos {
	

	@Autowired
	private MuseosRepository repoMuseos;

	@Override
	public List<Museo> obtenerMuseos() {
		return repoMuseos.findAll();
	}

	@Override
	public void agregar(Museo museo) {
		repoMuseos.save(museo);
	}

	@Override
	public Museo buscarPorId(Integer idMuseo) {
		Museo museo = null;
		Optional<Museo> optional = repoMuseos.findById(idMuseo);
		if(optional.isPresent()) {
			return optional.get();
		}
		return museo;
	}

	@Override
	public void eliminar(Integer idMuseo) {
	    repoMuseos.deleteById(idMuseo);
	}
	
	@Override
	public int totalMuseos() {
		return obtenerMuseos().size();
	}

	@Override
	public Page<Museo> buscarTodas(Pageable page) {
		return repoMuseos.findAll(page);
	}

	@Override
	public List<Museo> buscarPorEstatusYDestacado() {
	    EstatusMuseo estatus = EstatusMuseo.Activo;
	    return repoMuseos.findByDestacadoAndEstatusOrderByIdDesc(1, estatus);
	}


    @Override
    public Page<Museo> buscarPorNombre(String nombre, Pageable pageable) {
        return repoMuseos.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @Override
    public Page<Museo> buscarPorCategoria(Integer categoriaId, Pageable pageable) {
        return repoMuseos.findByCategoriaId(categoriaId, pageable);
    }

    @Override
    public Page<Museo> buscarPorNombreYcategoria(String nombre, Integer categoriaId, Pageable pageable) {
        return repoMuseos.findByNombreContainingIgnoreCaseAndCategoriaId(nombre, categoriaId, pageable);
    }

    @Override
	public boolean existePorNombre(String nombre) {
	    return repoMuseos.findByNombre(nombre) != null;
	}

	@Override
	public Museo buscarPorNombre(String nombre) {
		return repoMuseos.findByNombre(nombre);
 
	}

	

}
