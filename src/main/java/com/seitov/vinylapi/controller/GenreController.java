package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(description = "Get array of Genres", tags = "genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Genre.class))))})
    @GetMapping
    public List<Genre> getGenres() {
        return genreService.getGenres();
    }

    @Operation(description = "Get array of vinyls filtered by Genre", tags = "genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = VinylLightDto.class))))})
    @GetMapping("/{id}/vinyls")
    public List<VinylLightDto> getVinylsByGenre(@PathVariable Long id) {
        return genreService.getVinylsLightByGenre(id);
    }

}
