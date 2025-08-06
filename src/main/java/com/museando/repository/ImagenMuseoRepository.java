package com.museando.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museando.model.ImagenMuseo;

public interface ImagenMuseoRepository extends JpaRepository<ImagenMuseo, Integer> {
	List<ImagenMuseo> findByMuseo_Id(Integer idMuseo);
}
