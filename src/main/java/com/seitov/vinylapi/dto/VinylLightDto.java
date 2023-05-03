package com.seitov.vinylapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class VinylLightDto {

    private Long id;
    private String name;
    private Double price;
    private List<String> artist;
    private String format;
    private Long photoId;

}
