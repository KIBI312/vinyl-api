package com.seitov.vinylapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto {

    private Long id;
    @NotBlank(message = "Name should not be blank")
    private String name;
    @Size(max = 1000, message = "Description should be less than 1000 characters")
    private String description;
    private Long photoId;

}
