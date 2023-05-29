package com.seitov.vinylapi.repository;

import com.seitov.vinylapi.entity.Vinyl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VinylRepository extends JpaRepository<Vinyl, Long> {

    <T> Optional<T> readById(Long id, Class<T> type);
    boolean existsByGenres_Id(Long id);
    boolean existsByArtists_Id(Long id);

}
