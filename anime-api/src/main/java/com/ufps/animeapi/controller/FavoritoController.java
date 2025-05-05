package com.ufps.animeapi.controller;

import com.ufps.animeapi.model.Favorito;
import com.ufps.animeapi.model.Manga;
import com.ufps.animeapi.model.Usuario;
import com.ufps.animeapi.repository.FavoritoRepository;
import com.ufps.animeapi.repository.MangaRepository;
import com.ufps.animeapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios/{username}/favoritos")
public class FavoritoController {
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private MangaRepository mangaRepo;
    @Autowired private FavoritoRepository favRepo;

    @GetMapping
    public ResponseEntity<?> list(@PathVariable String username) {
        if (usuarioRepo.findByUsername(username).isEmpty())
            return ResponseEntity
                     .badRequest()
                     .body(Map.of("error", true, "msg", "Usuario no encontrado"));

        List<Map<String,Object>> favs = favRepo.findByUsuarioUsername(username).stream()
          .map(f -> {
             Map<String,Object> dto = new LinkedHashMap<>();
             dto.put("id",            f.getManga().getId());
             dto.put("nombre",        f.getManga().getNombre());
             dto.put("fechaLanzamiento", f.getManga().getFechaLanzamiento().toString());
             dto.put("temporadas",    f.getManga().getTemporadas());
             dto.put("pais",          f.getManga().getPais().getNombre());
             dto.put("anime",         f.getManga().isAnime());
             dto.put("juego",         f.getManga().isJuego());
             dto.put("pelicula",      f.getManga().isPelicula());
             dto.put("tipo",          f.getManga().getTipo().getNombre());
             return dto;
          })
          .collect(Collectors.toList());

        return ResponseEntity.ok(favs);
    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable String username, @RequestBody Map<String,Long> body) {
        Long mangaId = body.get("idManga");
        if (mangaId == null)
            return ResponseEntity.badRequest().body(Map.of("error",true,"msg","Falta idManga"));

        Usuario user = usuarioRepo.findByUsername(username)
            .orElse(null);
        if (user == null)
            return ResponseEntity.badRequest().body(Map.of("error",true,"msg","Usuario no encontrado"));

        Manga manga = mangaRepo.findById(mangaId)
            .orElse(null);
        if (manga == null)
            return ResponseEntity.badRequest().body(Map.of("error",true,"msg","Manga no encontrado"));

        if (favRepo.findByUsuarioUsernameAndMangaId(username, mangaId).isPresent())
            return ResponseEntity.badRequest().body(Map.of("error",true,"msg","Favorito ya se encuentra registrado"));

        Favorito fav = new Favorito();
        fav.setUsuario(user);
        fav.setManga(manga);
        favRepo.save(fav);
        return ResponseEntity.ok(Map.of("msg","Manga agregado a favoritos"));
    }

    @DeleteMapping("/{mangaId}")
    public ResponseEntity<?> delete(@PathVariable String username, @PathVariable Long mangaId) {
        if (usuarioRepo.findByUsername(username).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error",true,"msg","Usuario no encontrado"));
        if (mangaRepo.findById(mangaId).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error",true,"msg","Manga no encontrado"));

        if (favRepo.findByUsuarioUsernameAndMangaId(username, mangaId).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error",true,"msg","Favorito no encontrado"));

        favRepo.deleteByUsuarioUsernameAndMangaId(username, mangaId);
        return ResponseEntity.ok(Map.of("msg","Manga eliminado de favoritos"));
    }
}
