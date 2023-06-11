package com.seitov.vinylapi.repository;

import com.seitov.vinylapi.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    boolean existsByName(String name);
    List<Artist> findByPhotoId(Long id);

}
