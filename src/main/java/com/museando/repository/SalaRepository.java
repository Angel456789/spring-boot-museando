package com.museando.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museando.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Integer> {
	 List<Sala> findByMuseoId(Integer idMuseo);
}
