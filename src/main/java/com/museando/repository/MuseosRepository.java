package com.museando.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.museando.model.EstatusMuseo;
import com.museando.model.Museo;

public interface MuseosRepository extends JpaRepository<Museo, Integer> {
    
    // Busca museos destacados (por ejemplo destacado = 1)
    List<Museo> findByDestacado(int destacado);
    
    // Busca museos por estatus (por ejemplo "Activo")
    List<Museo> findByEstatus(String estatus);
    
    // Busca museos destacados y con cierto estatus, ordenados por id descendente
    List<Museo> findByDestacadoAndEstatusOrderByIdDesc(int destacado, EstatusMuseo estatus);
    
    Museo findByNombre(String nombre);
    
    
    Page<Museo> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Museo> findByCategoriaId(Integer categoriaId, Pageable pageable);

    Page<Museo> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Integer categoriaId, Pageable pageable);

}
