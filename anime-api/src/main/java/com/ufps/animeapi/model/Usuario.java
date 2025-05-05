package com.ufps.animeapi.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String nombre;
    private String email;
    private String password;

    @ManyToMany
    @JoinTable(
        name = "favorito",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "manga_id")
    )
    private List<Manga> favoritos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Manga> getFavoritos() {
		return favoritos;
	}

	public void setFavoritos(List<Manga> favoritos) {
		this.favoritos = favoritos;
	}
    
}
