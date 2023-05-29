package com.seitov.vinylapi.repository;

import com.seitov.vinylapi.entity.VinylShort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VinylShortRepository extends ReadOnlyRepository<VinylShort, Long> {

    @Query(
            value = "SELECT v.id, v.name, v.price, v.photo_low_res_id, v.format_id FROM vinyl v " +
                    "JOIN vinyl_genres vg ON vg.vinyl_id=v.id WHERE vg.genres_id=?1",
            nativeQuery = true
    )
    List<VinylShort> findAllByGenres_Id(Long id);
    @Query(
            value = "SELECT v.id, v.name, v.price, v.photo_low_res_id, v.format_id FROM vinyl v " +
                    "JOIN vinyl_artists va ON va.vinyl_id=v.id WHERE va.artists_id=?1",
            nativeQuery = true
    )
    List<VinylShort> findAllByArtists_Id(Long id);
    @Query(
            value = "SELECT v.id, v.name, v.price, v.photo_low_res_id, v.format_id FROM vinyl v " +
                    "WHERE v.format_id=?1",
            nativeQuery = true
    )
    List<VinylShort> findAllByFormat_Id(Long id);

}
