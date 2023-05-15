package com.seitov.vinylapi.repository;

import com.seitov.vinylapi.entity.Vinyl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VinylRepository extends JpaRepository<Vinyl, Long> {

    <T> List<T> findAllProjectedBy(Pageable pageable, Class<T> type);
    <T> List<T> readByArtists_Id(Long id, Class<T> type);
    <T> List<T> readByFormat_Id(Long id, Class<T> type);
    <T> List<T> readByGenres_Id(Long id, Class<T> type);
    <T> Optional<T> readById(Long id, Class<T> type);
    boolean existsByGenres_Id(Long id);

}
