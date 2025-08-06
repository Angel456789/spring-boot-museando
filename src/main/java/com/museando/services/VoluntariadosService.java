package com.museando.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.museando.model.EstadoVoluntariado;
import com.museando.model.Voluntariado;
import com.museando.repository.VoluntariadosRepository;

@Service
public class VoluntariadosService implements IntVoluntariados {

	@Autowired
	private VoluntariadosRepository repo;

	@Override
	public List<Voluntariado> obtenerTodos() {
		return repo.findAll();
	}

	@Override
	public Voluntariado buscarPorId(Integer id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public void guardar(Voluntariado voluntariado) {
		repo.save(voluntariado);
	}

	@Override
	public void eliminar(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public Page<Voluntariado> buscarTodas(Pageable page) {
		return repo.findAll(page);
	}

	@Override
	public List<Voluntariado> buscarPorEstado(EstadoVoluntariado estado) {
		return repo.findByEstado(estado);
	}

	@Override
	public List<Voluntariado> buscarPorUsuarioId(Integer idUsuario) {
		return repo.findByUsuarioId(idUsuario);
	}

	 public boolean existeSolicitudParaUsuarioYMuseo(Integer idUsuario, Integer idMuseo) {
	        List<Voluntariado> solicitudes = repo.findByUsuarioIdAndMuseoId(idUsuario, idMuseo);
	        for (Voluntariado v : solicitudes) {
	            if (v.getEstado() == EstadoVoluntariado.PENDIENTE || v.getEstado() == EstadoVoluntariado.ACEPTADO) {
	                return true;
	            }
	        }
	        return false;
	    }
	 
		@Override

	 public List<Voluntariado> obtenerPorMuseo(int idMuseo) {
		    return repo.findByMuseoId(idMuseo);
		}


}
