package com.seitov.vinylapi.dto;

import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.projection.ArtistName;
import lombok.Data;

import java.util.List;

@Data
public class VinylLightDto {

    private Long id;
    private String name;
    private Double price;
    private List<ArtistName> artists;
    private Format format;
    private Long photoId;

}
