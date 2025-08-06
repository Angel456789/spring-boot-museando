package com.museando.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.museando.model.EstadoVoluntariado;
import com.museando.model.Voluntariado;

public interface IntVoluntariados {

    List<Voluntariado> obtenerTodos();

    Voluntariado buscarPorId(Integer id);

    void guardar(Voluntariado voluntariado);

    void eliminar(Integer id);
    
  	Page<Voluntariado> buscarTodas(Pageable page);
  	
  	List<Voluntariado> buscarPorEstado(EstadoVoluntariado estado);

  	List<Voluntariado> buscarPorUsuarioId(Integer idUsuario);
    boolean existeSolicitudParaUsuarioYMuseo(Integer idUsuario, Integer idMuseo);
    List<Voluntariado> obtenerPorMuseo(int idMuseo);




}
