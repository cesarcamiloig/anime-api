package com.ufps.animeapi.controller;

import com.ufps.animeapi.model.Manga;
import com.ufps.animeapi.model.Pais;
import com.ufps.animeapi.model.Tipo;
import com.ufps.animeapi.repository.MangaRepository;
import com.ufps.animeapi.repository.PaisRepository;
import com.ufps.animeapi.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/mangas")
public class MangaController {

    @Autowired
    private MangaRepository mangaRepo;

    @Autowired
    private PaisRepository paisRepo;

    @Autowired
    private TipoRepository tipoRepo;

    // 1. Obtener todos los mangas
    @GetMapping
    public List<Map<String, Object>> getAllMangas() {
        return mangaRepo.findAll().stream().map(this::toDto).toList();
    }

    // 2. Obtener un manga por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMangaById(@PathVariable Long id) {
        Optional<Manga> manga = mangaRepo.findById(id);
        if (manga.isPresent()) {
            return ResponseEntity.ok(toDto(manga.get()));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Objeto no encontrado"));
        }
    }

    // 3. Crear un nuevo manga
    @PostMapping
    public ResponseEntity<?> createManga(@RequestBody Map<String, Object> data) {
        try {
            // Validación de campos obligatorios
            for (String campo : List.of("nombre", "fechaLanzamiento", "temporadas", "paisId", "tipoId")) {
                if (!data.containsKey(campo)) {
                    return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "El campo " + campo + " es obligatorio"));
                }
            }

            Long paisId = Long.valueOf(data.get("paisId").toString());
            Long tipoId = Long.valueOf(data.get("tipoId").toString());

            Optional<Pais> pais = paisRepo.findById(paisId);
            Optional<Tipo> tipo = tipoRepo.findById(tipoId);

            if (pais.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "País no existe"));
            }

            if (tipo.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Tipo no existe"));
            }

            Manga manga = new Manga();
            manga.setNombre((String) data.get("nombre"));
            manga.setFechaLanzamiento(java.time.LocalDate.parse((String) data.get("fechaLanzamiento")));
            manga.setTemporadas((int) data.get("temporadas"));
            manga.setAnime((boolean) data.getOrDefault("anime", false));
            manga.setJuego((boolean) data.getOrDefault("juego", false));
            manga.setPelicula((boolean) data.getOrDefault("pelicula", false));
            manga.setPais(pais.get());
            manga.setTipo(tipo.get());

            mangaRepo.save(manga);

            return ResponseEntity.ok(toDto(manga));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", true, "msg", "Error interno"));
        }
    }

    // Método para convertir entidad a DTO compatible con el frontend
    private Map<String, Object> toDto(Manga manga) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", manga.getId());
        dto.put("nombre", manga.getNombre());
        dto.put("fechaLanzamiento", manga.getFechaLanzamiento().toString());
        dto.put("temporadas", manga.getTemporadas());
        dto.put("pais", manga.getPais().getNombre());
        dto.put("anime", manga.getAnime());
        dto.put("juego", manga.getJuego());
        dto.put("pelicula", manga.getPelicula());
        dto.put("tipo", manga.getTipo().getNombre());
        return dto;
    }
    
 // 4. Actualizar un manga existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateManga(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        Optional<Manga> optManga = mangaRepo.findById(id);
        if (optManga.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Objeto no encontrado"));
        }

        // Validar campos obligatorios
        for (String campo : List.of("nombre", "fechaLanzamiento", "temporadas", "paisId", "tipoId")) {
            if (!data.containsKey(campo)) {
                return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "El campo " + campo + " es obligatorio"));
            }
        }

        Long paisId = Long.valueOf(data.get("paisId").toString());
        Long tipoId = Long.valueOf(data.get("tipoId").toString());

        Optional<Pais> pais = paisRepo.findById(paisId);
        Optional<Tipo> tipo = tipoRepo.findById(tipoId);

        if (pais.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "País no existe"));
        }

        if (tipo.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Tipo no existe"));
        }

        Manga manga = optManga.get();
        manga.setNombre((String) data.get("nombre"));
        manga.setFechaLanzamiento(java.time.LocalDate.parse((String) data.get("fechaLanzamiento")));
        manga.setTemporadas((int) data.get("temporadas"));
        manga.setAnime((boolean) data.getOrDefault("anime", false));
        manga.setJuego((boolean) data.getOrDefault("juego", false));
        manga.setPelicula((boolean) data.getOrDefault("pelicula", false));
        manga.setPais(pais.get());
        manga.setTipo(tipo.get());

        mangaRepo.save(manga);

        return ResponseEntity.ok(toDto(manga));
    }

    // 5. Eliminar un manga
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManga(@PathVariable Long id) {
        Optional<Manga> manga = mangaRepo.findById(id);
        if (manga.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", true, "msg", "Objeto no encontrado"));
        }

        mangaRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("msg", "Manga eliminado"));
    }
    
}
