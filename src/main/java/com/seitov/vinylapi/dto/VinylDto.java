package com.seitov.vinylapi.dto;

import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.projection.ArtistName;
import lombok.Data;

import java.util.List;

@Data
public class VinylDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private List<ArtistName> artists;
    private List<Genre> genres;
    private Format format;
    private Boolean inStock;
    private String recordLabel;
    private List<String> trackList;
    private Long photoId;

}
