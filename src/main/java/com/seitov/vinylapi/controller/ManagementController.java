package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.dto.ResponseMessage;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.service.ManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @Operation(description = "Creates new genre resource", tags = "management")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResourceId.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @PostMapping(value = "/genres", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResourceId newGenre(@RequestBody Genre genre) {
        return managementService.createGenre(genre);
    }

    @Operation(description = "Deletes genre by id", tags = "management")
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
    @DeleteMapping("/genres")
    public ResponseMessage deleteGenre(@RequestBody ResourceId genreId) {
        managementService.deleteGenre(genreId);
        return new ResponseMessage(200, "SUCCESSFUL_DELETION",
                "Genre with id " + genreId.getId() + " was deleted!");
    }

    @Operation(description = "Uploads photo", tags = "management")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResourceId.class))),
            @ApiResponse(responseCode = "500", description = "Error during processing",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResourceId uploadPhoto(@RequestParam("photo") MultipartFile imageContent) {
       return managementService.createPhoto(imageContent);
    }

    @PostMapping(value = "/artists")
    public ResourceId createArtist(@Valid @RequestBody ArtistDto artistDto) {
        return managementService.createArtist(artistDto);
    }

}
