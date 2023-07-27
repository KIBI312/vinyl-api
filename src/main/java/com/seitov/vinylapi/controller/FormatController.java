package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.entity.VinylShort;
import com.seitov.vinylapi.service.FormatService;
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
@RequestMapping("/formats")
public class FormatController {

    private final FormatService formatService;

    public FormatController(FormatService formatService) {
        this.formatService = formatService;
    }

    @Operation(description = "Get array of Formats", tags = "format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Format.class))))})
    @GetMapping
    public List<Format> getFormats() {
        return formatService.getFormats();
    }

    @Operation(description = "Get array of vinyls filtered by Format", tags = "format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = VinylShort.class))))})
    @GetMapping("/{id}/vinyls")
    public List<VinylShort> getVinylsByFormat(@PathVariable Long id) {
        return formatService.getVinylsShortByFormat(id);
    }

}
