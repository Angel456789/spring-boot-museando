package com.museando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.museando.model.Sala;
import com.museando.repository.SalaRepository;

@Service
public class ServiceSalasInt implements IntSalas {

	@Autowired
	private SalaRepository repoSalas;

	@Override
	public List<Sala> obtenerSalas() {
		return repoSalas.findAll();
	}

	@Override
	public void agregar(Sala sala) {
		repoSalas.save(sala);
	}

	@Override
	public Sala buscarPorId(Integer idSala) {
		Sala sala = null;
		Optional<Sala> optional = repoSalas.findById(idSala);
		if (optional.isPresent()) {
			return optional.get();
		}
		return sala;
	}

	@Override
	public void eliminar(Integer idSala) {
		repoSalas.deleteById(idSala);
	}

	@Override
	public int totalSalas() {
		return obtenerSalas().size();
	}

	@Override
	public Page<Sala> buscarTodas(Pageable page) {
		return repoSalas.findAll(page);
	}

	@Override
	public List<Sala> buscarPorMuseo(Integer idMuseo) {
		return repoSalas.findByMuseoId(idMuseo);
	}

}
