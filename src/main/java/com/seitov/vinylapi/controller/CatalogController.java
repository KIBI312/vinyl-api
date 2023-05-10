package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ResponseMessage;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.dto.VinylLightDto;
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

    private final ResponseMessage noFoundExample = new ResponseMessage();

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Operation(description = "Get array of vinyls",tags = "catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = VinylLightDto.class))))})
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



}
