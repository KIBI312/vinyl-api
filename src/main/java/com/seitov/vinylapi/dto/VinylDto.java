package com.seitov.vinylapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class VinylDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private List<String> artists;
    private List<String> genres;
    private String format;
    private Boolean inStock;
    private String recordLabel;
    private List<String> trackList;
    private Long photoId;

}
