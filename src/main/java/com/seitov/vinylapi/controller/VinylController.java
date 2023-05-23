package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ResponseMessage;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.service.VinylService;
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
@RequestMapping(value = "/vinyls")
public class VinylController {

    private final VinylService vinylService;

    public VinylController(VinylService vinylService) {
        this.vinylService = vinylService;
    }

    @Operation(description = "Get array of vinyls",tags = "vinyl")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = VinylLightDto.class)))),
            @ApiResponse(responseCode = "400", description = "Missing required 'page' parameter",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseMessage.class)))})
    @GetMapping
    public List<VinylLightDto> getVinyls(@RequestParam Integer page) {
        return vinylService.getVinylsLight(page);
    }

    @Operation(description = "Get detailed vinyl object", tags = "vinyl")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = VinylDto.class))),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ResponseMessage.class)))
    })
    @GetMapping("/{id}")
    public VinylDto getVinyl(@PathVariable Long id) {
        return vinylService.getVinylById(id);
    }

}
