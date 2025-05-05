package com.ufps.animeapi.repository;

import com.ufps.animeapi.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    /**
     * Obtiene todos los favoritos de un usuario por su username.
     */
    List<Favorito> findByUsuarioUsername(String username);

    /**
     * Busca un favorito específico por username de usuario y id de manga.
     */
    Optional<Favorito> findByUsuarioUsernameAndMangaId(String username, Long mangaId);

    /**
     * Elimina un favorito por username de usuario y id de manga.
     */
    void deleteByUsuarioUsernameAndMangaId(String username, Long mangaId);
}
