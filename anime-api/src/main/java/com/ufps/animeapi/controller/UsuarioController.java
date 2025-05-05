package com.ufps.animeapi.controller;

import com.ufps.animeapi.model.Manga;
import com.ufps.animeapi.model.Usuario;
import com.ufps.animeapi.repository.MangaRepository;
import com.ufps.animeapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private MangaRepository mangaRepo;

    // 1. Obtener mangas favoritos de un usuario
    @GetMapping("/{username}/favoritos")
    public ResponseEntity<?> getFavoritos(@PathVariable String username) {
        Optional<Usuario> optUsuario = usuarioRepo.findByUsername(username);
        if (optUsuario.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Usuario no encontrado"));
        }

        List<Map<String, Object>> favoritos = optUsuario.get().getFavoritos().stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(favoritos);
    }

    // 2. Agregar un manga a favoritos
    @PostMapping("/{username}/favoritos")
    public ResponseEntity<?> agregarFavorito(@PathVariable String username, @RequestBody Map<String, Object> body) {
        if (!body.containsKey("idManga")) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Falta idManga"));
        }

        Optional<Usuario> optUsuario = usuarioRepo.findByUsername(username);
        Optional<Manga> optManga = mangaRepo.findById(Long.parseLong(body.get("idManga").toString()));

        if (optUsuario.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Usuario no encontrado"));
        }

        if (optManga.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Manga no encontrado"));
        }

        Usuario usuario = optUsuario.get();
        Manga manga = optManga.get();

        if (!usuario.getFavoritos().contains(manga)) {
            usuario.getFavoritos().add(manga);
            usuarioRepo.save(usuario);
        }

        return ResponseEntity.ok(Map.of("msg", "Manga agregado a favoritos"));
    }

    // 3. Eliminar un manga de favoritos
    @DeleteMapping("/{username}/favoritos/{idManga}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable String username, @PathVariable Long idManga) {
        Optional<Usuario> optUsuario = usuarioRepo.findByUsername(username);
        Optional<Manga> optManga = mangaRepo.findById(idManga);

        if (optUsuario.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Usuario no encontrado"));
        }

        if (optManga.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Manga no encontrado"));
        }

        Usuario usuario = optUsuario.get();
        usuario.getFavoritos().remove(optManga.get());
        usuarioRepo.save(usuario);

        return ResponseEntity.ok(Map.of("msg", "Manga eliminado de favoritos"));
    }

    // Reutilizamos el mismo DTO del controlador de mangas
    private Map<String, Object> toDto(Manga manga) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", manga.getId());
        dto.put("nombre", manga.getNombre());
        dto.put("fechaLanzamiento", manga.getFechaLanzamiento().toString());
        dto.put("temporadas", manga.getTemporadas());
        dto.put("pais", manga.getPais().getNombre());
        dto.put("anime", manga.isAnime());
        dto.put("juego", manga.isJuego());
        dto.put("pelicula", manga.isPelicula());
        dto.put("tipo", manga.getTipo().getNombre());
        return dto;
    }
}
