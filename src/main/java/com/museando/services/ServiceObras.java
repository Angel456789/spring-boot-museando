package com.museando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.museando.model.Obra;
import com.museando.repository.ObraRepository;

@Service
public class ServiceObras implements IntObras {

	@Autowired
	private ObraRepository repoObras;
	
	@Override
	public List<Obra> obtenerObras() {
		return repoObras.findAll();
	}

	@Override
	public List<Obra> buscarPorSala(Integer idSala) {
        return repoObras.findBySalaId(idSala);
	}

	@Override
	public void agregar(Obra obra) {
		repoObras.save(obra);
	}

	@Override
	public Obra buscarPorId(Integer idObra) {
		Obra obra = null;
		Optional<Obra> optional = repoObras.findById(idObra);
		if(optional.isPresent()) {
			return optional.get();
		}
		return obra;
	}

	@Override
	public void eliminar(Integer idObra) {
		repoObras.deleteById(idObra);
	}

	@Override
	public int totalObras() {
		return obtenerObras().size();
	}

	@Override
	public Page<Obra> buscarTodas(Pageable page) {
		return repoObras.findAll(page);
	}

}
