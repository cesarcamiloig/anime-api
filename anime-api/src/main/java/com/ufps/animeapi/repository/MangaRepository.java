package com.ufps.animeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufps.animeapi.model.Manga;

public interface MangaRepository extends JpaRepository<Manga, Long> {
	
	
	
}