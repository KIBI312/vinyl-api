package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.entity.ArtistShort;
import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.entity.VinylShort;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.service.VinylService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
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
public class VinylControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VinylService vinylService;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Test
    public void getAllVinyls() throws Exception {
        //given
        List<VinylShort> vinylShorts = new ArrayList<>();
        for (long i = 0; i < 3; i++){
            vinylShorts.add(new VinylShort(i, "Vinyl #"+i,
                    null, null, null, null));
        }
        //when
        when(vinylService.getVinylsShort(0)).thenReturn(vinylShorts);
        //then
        mockMvc.perform(get("/vinyls?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("[0].id", is(0)))
                .andExpect(jsonPath("[0].name", is("Vinyl #0")));
    }

    @Test
    public void getVinylNonExisting() throws Exception {
        //when
        when(vinylService.getVinylById(1L)).thenThrow(new ResourceNotFoundException("Vinyl with this id doesn't exist"));
        //then
        mockMvc.perform(get("/vinyls/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.type", is("NOT_EXIST")))
                .andExpect(jsonPath("$.message", is("Vinyl with this id doesn't exist")));
    }

    @Test
    public void  getVinyl() throws Exception {
        //given
        ArtistShort artistShort = new ArtistShort(1L, "MichaelJ");
        Format format = new Format(1L, "2LP");
        Genre genre = new Genre(1L, "Pop");
        VinylDto vinylDto = new VinylDto();
        vinylDto.setId(0L);
        vinylDto.setName("Moonwalk");
        vinylDto.setDescription("Legendary album of legendary artist");
        vinylDto.setPrice(20.99);
        vinylDto.setArtists(List.of(artistShort));
        vinylDto.setGenres(List.of(genre));
        vinylDto.setFormat(format);
        vinylDto.setInStock(true);
        vinylDto.setRecordLabel("EMI");
        vinylDto.setTrackList(List.of("Billy jeans", "Smooth criminal"));
        vinylDto.setPhotoId(1L);
        //when
        when(vinylService.getVinylById(0L)).thenReturn(vinylDto);
        //then
        mockMvc.perform(get("/vinyls/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.name", is("Moonwalk")))
                .andExpect(jsonPath("$.description", is("Legendary album of legendary artist")))
                .andExpect(jsonPath("$.price", is(20.99)))
                .andExpect(jsonPath("$.artists[0].name", is("MichaelJ")))
                .andExpect(jsonPath("$.genres[0].name", is("Pop")))
                .andExpect(jsonPath("$.format.name", is("2LP")))
                .andExpect(jsonPath("$.inStock", is(true)))
                .andExpect(jsonPath("$.recordLabel", is("EMI")))
                .andExpect(jsonPath("$.trackList", hasSize(2)))
                .andExpect(jsonPath("$.photoId", is(1)));
    }

}
