package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CatalogService catalogService;

    @Test
    public void getAllVinyls() throws Exception {
        //given
        List<VinylLightDto> vinylLightDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            VinylLightDto vinylLightDto = new VinylLightDto();
            vinylLightDto.setId((long) i);
            vinylLightDto.setName("Vinyl #"+i);
            vinylLightDtos.add(vinylLightDto);
        }
        //when
        when(catalogService.getVinylsLight(0)).thenReturn(vinylLightDtos);
        //then
        mockMvc.perform(get("/catalog/vinyls?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("[0].id", is(vinylLightDtos.get(0).getId()), Long.class))
                .andExpect(jsonPath("[0].name", is(vinylLightDtos.get(0).getName())));
    }

    @Test
    public void getVinylNonExisting() throws Exception {
        //when
        when(catalogService.getVinylById(1L)).thenThrow(new ResourceNotFoundException("Vinyl with this id doesn't exist"));
        //then
        mockMvc.perform(get("/catalog/vinyls/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.type", is("NOT_EXIST")))
                .andExpect(jsonPath("$.message", is("Vinyl with this id doesn't exist")));
    }

    @Test
    public void  getVinyl() throws Exception {
        //given
        VinylDto vinylDto = new VinylDto();
        vinylDto.setId(0L);
        vinylDto.setName("Moonwalk");
        vinylDto.setDescription("Legendary album of legendary artist");
        vinylDto.setPrice(20.99);
        vinylDto.setArtists(List.of("MichaelJ"));
        vinylDto.setGenres(List.of("Pop"));
        vinylDto.setFormat("Pop");
        vinylDto.setInStock(true);
        vinylDto.setRecordLabel("EMI");
        vinylDto.setTrackList(List.of("Billy jeans", "Smooth criminal"));
        vinylDto.setPhotoId(1L);
        //when
        when(catalogService.getVinylById(0L)).thenReturn(vinylDto);
        //then
        mockMvc.perform(get("/catalog/vinyls/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.name", is("Moonwalk")))
                .andExpect(jsonPath("$.description", is("Legendary album of legendary artist")))
                .andExpect(jsonPath("$.price", is(20.99)))
                .andExpect(jsonPath("$.artists[0]", is("MichaelJ")))
                .andExpect(jsonPath("$.genres[0]", is("Pop")))
                .andExpect(jsonPath("$.format", is("Pop")))
                .andExpect(jsonPath("$.inStock", is(true)))
                .andExpect(jsonPath("$.recordLabel", is("EMI")))
                .andExpect(jsonPath("$.trackList", hasSize(2)))
                .andExpect(jsonPath("$.photoId", is(1)));
    }

}
