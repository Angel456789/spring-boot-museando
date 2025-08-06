package com.museando.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museando.model.Obra;

public interface ObraRepository extends JpaRepository<Obra, Integer> {
    List<Obra> findBySalaId(Integer idSala);

}
