package com.museando.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museando.model.Usuario;

public interface IntUsuarios {
	
	public List<Usuario> obtenerUsuarios();
    public void agregar(Usuario usuario);
    public Usuario buscarPorId(Integer idUsuario);
    public void eliminar(Integer idUsuario);
    public int totalUsuarios();

  //nueva logica
  	Page<Usuario> buscarTodas(Pageable page);

    // ✅ Para validar si ya existe ese username o email
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    Usuario buscarPorUsuario(String username);
	public Usuario buscarPorUsername(String username);
    Usuario buscarPorEmail(String email);

}
