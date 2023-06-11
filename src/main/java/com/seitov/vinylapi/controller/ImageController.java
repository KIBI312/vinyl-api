package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.dto.ResponseMessage;
import com.seitov.vinylapi.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photo")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(description = "Get photo by id", tags = "photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "Photo with this id doesn't exist",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    private Resource getImage(@PathVariable Long id) {
        byte[] image = imageService.getPhoto(id);
        return new ByteArrayResource(image);
    }

    @Operation(description = "Uploads photo", tags = "photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResourceId.class))),
            @ApiResponse(responseCode = "500", description = "Error during processing",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResourceId uploadPhoto(@RequestParam("photo") MultipartFile imageContent) {
        return imageService.createPhoto(imageContent);
    }

    @Operation(description = "Deletes photo", tags = "photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "404", description = "Trying to delete non-existing genre",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @DeleteMapping(value = "/{id}")
    public ResponseMessage deletePhoto(@PathVariable Long id) {
        imageService.deletePhoto(id);
        return new ResponseMessage(200, "SUCCESSFUL_DELETION",
                "Photo with id " + id + " was deleted!");
    }

}
