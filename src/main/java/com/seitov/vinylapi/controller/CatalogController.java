package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResponseMessage;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.service.CatalogService;
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
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Operation(description = "Get array of vinyls",tags = "catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = VinylLightDto.class)))),
            @ApiResponse(responseCode = "400", description = "Missing required 'page' parameter",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @GetMapping("/vinyls")
    public List<VinylLightDto> getVinyls(@RequestParam Integer page) {
        return catalogService.getVinylsLight(page);
    }

    @Operation(description = "Get detailed vinyl object", tags = "catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = VinylDto.class))),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseMessage.class)))
    })
    @GetMapping("/vinyls/{id}")
    public VinylDto getVinyl(@PathVariable Long id) {
        return catalogService.getVinylById(id);
    }

    @Operation(description = "Get array of vinyls filtered by Artist", tags = "catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = VinylLightDto.class))))})
    @GetMapping("/artists/{id}/vinyls")
    public List<VinylLightDto> getVinylsByArtist(@PathVariable Long id) {
        return catalogService.getVinylsLightByArtist(id);
    }


    @Operation(description = "Get array of vinyls filtered by Genre", tags = "catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = VinylLightDto.class))))})
    @GetMapping("/genres/{id}/vinyls")
    public List<VinylLightDto> getVinylsByGenre(@PathVariable Long id) {
        return catalogService.getVinylsLightByGenre(id);
    }


    @Operation(description = "Get array of vinyls filtered by Format", tags = "catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = VinylLightDto.class))))})
    @GetMapping("/formats/{id}/vinyls")
    public List<VinylLightDto> getVinylsByFormat(@PathVariable Long id) {
        return catalogService.getVinylsLightByFormat(id);
    }

    @Operation(description = "Get array of Formats", tags = "catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Format.class))))})
    @GetMapping("/formats")
    public List<Format> getFormats() {
        return catalogService.getFormats();
    }

    @Operation(description = "Get array of Artists", tags = "catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ArtistDto.class)))),
            @ApiResponse(responseCode = "400", description = "Missing required 'page' parameter",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseMessage.class)))})
    @GetMapping("/artists")
    public List<ArtistDto> getArtists(@RequestParam Integer page) {
        return catalogService.getArtists(page);
    }

}
