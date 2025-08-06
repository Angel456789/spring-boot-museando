package com.museando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museando.model.Categoria;

public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {
	Categoria findByNombre(String nombre);

}
