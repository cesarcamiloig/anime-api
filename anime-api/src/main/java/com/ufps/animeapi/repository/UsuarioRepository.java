package com.ufps.animeapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ufps.animeapi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
    Optional<Usuario> findByUsername(String username);
    
}
