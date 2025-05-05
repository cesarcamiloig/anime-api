package com.ufps.animeapi.controller;

import com.ufps.animeapi.model.Usuario;
import com.ufps.animeapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired private UsuarioRepository usuarioRepo;

    @GetMapping("/{username}")
    public ResponseEntity<?> getUsuario(@PathVariable String username) {
        Optional<Usuario> opt = usuarioRepo.findByUsername(username);
        if (opt.isEmpty()) {
            Map<String,Object> error = Map.of("error", true, "msg", "Usuario no encontrado");
            return ResponseEntity.badRequest().body(error);
        }

        Usuario u = opt.get();
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", u.getId());
        dto.put("username", u.getUsername());
        dto.put("nombre", u.getNombre());
        dto.put("email", u.getEmail());
        return ResponseEntity.ok(dto);
    }
}
