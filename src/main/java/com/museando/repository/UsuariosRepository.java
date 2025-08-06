package com.museando.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.museando.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
	  // 🔍 Para buscar un usuario completo por su email o username
    Usuario findByEmail(String email);
    Usuario findByUsername(String username);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
