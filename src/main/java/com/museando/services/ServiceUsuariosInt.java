package com.museando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.museando.model.Usuario;
import com.museando.repository.UsuariosRepository;

@Service
public class ServiceUsuariosInt implements IntUsuarios {
	
	@Autowired
	private UsuariosRepository repoUsuarios;

	@Override
	public List<Usuario> obtenerUsuarios() {
		return repoUsuarios.findAll();
	}

	@Override
	public void agregar(Usuario usuario) {
		repoUsuarios.save(usuario);
	}

	@Override
	public Usuario buscarPorId(Integer idUsuario) {
		Usuario usuario = null;
		Optional<Usuario>optional =repoUsuarios.findById(idUsuario);
		if(optional.isPresent()) {
			usuario = optional.get();
		}
		return usuario;
	}

	@Override
	public void eliminar(Integer idUsuario) {
        repoUsuarios.deleteById(idUsuario);
	}

	@Override
	public int totalUsuarios() {
		return obtenerUsuarios().size();
	}

	@Override
	public Page<Usuario> buscarTodas(Pageable page) {
		return repoUsuarios.findAll(page);
	}

	@Override
	public boolean existsByUsername(String username) {
		return repoUsuarios.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return repoUsuarios.existsByEmail(email);
	}
	
	@Override
	public Usuario buscarPorUsuario(String username) {
	    return repoUsuarios.findByUsername(username);
	}
	
	 @Override
	    public Usuario buscarPorEmail(String email) {
	        return repoUsuarios.findByEmail(email);
	    }


    @Override
    public Usuario buscarPorUsername(String username) {
        return repoUsuarios.findByUsername(username);
    }

}
