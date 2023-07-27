package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.dto.ResponseMessage;
import com.seitov.vinylapi.entity.VinylShort;
import com.seitov.vinylapi.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Operation(description = "Get array of Artists", tags = "artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ArtistDto.class)))),
            @ApiResponse(responseCode = "400", description = "Missing required 'page' parameter",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @GetMapping
    public List<ArtistDto> getArtists(@RequestParam Integer page) {
        return artistService.getArtists(page);
    }

    @Operation(description = "Get array of vinyls filtered by Artist", tags = "artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = VinylShort.class))))})
    @GetMapping("/{id}/vinyls")
    public List<VinylShort> getVinylsByArtist(@PathVariable Long id) {
        return artistService.getVinylsShortByArtist(id);
    }

    @Operation(description = "Creates new Artist resource", tags = "artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResourceId.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "409", description = "Trying to save with non-existing photo",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))
    })
    @PostMapping
    public ResourceId createArtist(@Valid @RequestBody ArtistDto artistDto) {
        return artistService.createArtist(artistDto);
    }

    @Operation(description = "Deletes artist by id", tags = "artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "404", description = "Trying to delete non-existing artist",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "409", description = "Trying to delete artist with dependent vinyls",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @DeleteMapping("/{id}")
    public ResponseMessage deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return new ResponseMessage(200, "SUCCESSFUL_DELETION",
                "Artists with id " + id + " was deleted!");
    }

}
