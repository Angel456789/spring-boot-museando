package com.museando.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museando.model.EstadoVoluntariado;
import com.museando.model.Voluntariado;

public interface VoluntariadosRepository extends JpaRepository<Voluntariado, Integer> {
    List<Voluntariado> findByEstado(EstadoVoluntariado estado);
    List<Voluntariado> findByUsuarioId(Integer idUsuario);
    List<Voluntariado> findByUsuarioIdAndMuseoId(Integer usuarioId, Integer museoId);
    List<Voluntariado> findByMuseoId(int idMuseo);

}
