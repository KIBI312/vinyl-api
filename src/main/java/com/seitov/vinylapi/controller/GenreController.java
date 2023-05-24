package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.dto.ResponseMessage;
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
import org.springframework.web.bind.annotation.*;

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

    @Operation(description = "Creates new genre resource", tags = "genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResourceId.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResourceId newGenre(@RequestBody Genre genre) {
        return genreService.createGenre(genre);
    }

    @Operation(description = "Deletes genre by id", tags = "genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "404", description = "Trying to delete non-existing genre",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "409", description = "Trying to delete genre with dependent vinyls",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @DeleteMapping("/{id}")
    public ResponseMessage deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return new ResponseMessage(200, "SUCCESSFUL_DELETION",
                "Genre with id " + id + " was deleted!");
    }

}
