package com.ufps.animeapi.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Manga {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private LocalDate fechaLanzamiento;

    private Integer temporadas;

    private boolean anime;
    private boolean juego;
    private boolean pelicula;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private Tipo tipo;

    @ManyToMany(mappedBy = "favoritos")
    private List<Usuario> usuariosFavoritos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaLanzamiento() {
		return fechaLanzamiento;
	}

	public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
		this.fechaLanzamiento = fechaLanzamiento;
	}

	public Integer getTemporadas() {
		return temporadas;
	}

	public void setTemporadas(Integer temporadas) {
		this.temporadas = temporadas;
	}

	public boolean isAnime() {
		return anime;
	}

	public void setAnime(boolean anime) {
		this.anime = anime;
	}

	public boolean isJuego() {
		return juego;
	}

	public void setJuego(boolean juego) {
		this.juego = juego;
	}

	public boolean isPelicula() {
		return pelicula;
	}

	public void setPelicula(boolean pelicula) {
		this.pelicula = pelicula;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public List<Usuario> getUsuariosFavoritos() {
		return usuariosFavoritos;
	}

	public void setUsuariosFavoritos(List<Usuario> usuariosFavoritos) {
		this.usuariosFavoritos = usuariosFavoritos;
	}
    
}
