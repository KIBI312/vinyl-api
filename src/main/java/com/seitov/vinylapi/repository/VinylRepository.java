package com.seitov.vinylapi.repository;

import com.seitov.vinylapi.entity.Vinyl;
import com.seitov.vinylapi.projection.VinylLight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VinylRepository extends JpaRepository<Vinyl, Long> {

    Optional<VinylLight> readById(Long id);

}
