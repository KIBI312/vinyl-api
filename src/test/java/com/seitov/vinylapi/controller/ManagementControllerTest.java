package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.service.ManagementService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagementService managementService;

    @Test
    public void genreCreationTest() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Rock");
        Genre genre = new Genre();
        genre.setName("Rock");
        //when
        when(managementService.createGenre(genre)).thenReturn(new ResourceId(1L));
        //then
        mockMvc.perform(post("/management/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void genreCreationWithRedundantId() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("name", "Rock");
        Genre genre = new Genre(1L, "Rock");
        //when
        when(managementService.createGenre(genre)).thenThrow(new RedundantPropertyException("Id not allowed in POST requests"));
        //then
        mockMvc.perform(post("/management/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.type", is("REDUNDANT_PROPERTY")))
                .andExpect(jsonPath("$.message", is("Id not allowed in POST requests")));
    }

    @Test
    public void genreCreationDuplication() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Rock");
        Genre genre = new Genre();
        genre.setName("Rock");
        //when
        when(managementService.createGenre(genre)).thenThrow(new ResourceAlreadyExistsException("This genre already exists"));
        //then
        mockMvc.perform(post("/management/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.type", is("RESOURCE_DUPLICATION")))
                .andExpect(jsonPath("$.message", is("This genre already exists")));
    }

    @Test
    public void genreDeletionTest() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        //then
        mockMvc.perform(delete("/management/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.type", is("SUCCESSFUL_DELETION")))
                .andExpect(jsonPath("$.message", is("Genre with id 1 was deleted!")));
    }

    @Test
    public void nonExistingGenreDeletion() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        ResourceId genreId = new ResourceId(1L);
        //when
        doThrow(new ResourceNotFoundException("Genre with this id doesn't exist"))
                .when(managementService).deleteGenre(genreId);
        //then
        mockMvc.perform(delete("/management/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.type", is("NOT_EXIST")))
                .andExpect(jsonPath("$.message", is("Genre with this id doesn't exist")));
    }

    @Test
    public void genreWithVinylsDeletion() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        ResourceId genreId = new ResourceId(1L);
        //when
        doThrow(new DataConstraintViolationException("Cannot delete while there's vinyls dependent from this genre!"))
                .when(managementService).deleteGenre(genreId);
        //then
        mockMvc.perform(delete("/management/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(409)))
                .andExpect(jsonPath("$.type", is("DATA_CONSTRAINT_VIOLATION")))
                .andExpect(jsonPath("$.message", is("Cannot delete while there's vinyls dependent from this genre!")));
    }

    @Test
    public void createArtist() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Jake");
        jsonObject.put("description", "SomeArtist");
        jsonObject.put("photoId", 1);
        ArtistDto artistDto = new ArtistDto(null, "Jake", "SomeArtist", 1L);
        ResourceId result = new ResourceId(0L);
        //when
        when(managementService.createArtist(artistDto)).thenReturn(result);
        //then
        mockMvc.perform(post("/management/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0)));
    }

    @Test
    public void createArtistWithBlankName() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "");
        jsonObject.put("description", "SomeArtist");
        jsonObject.put("photoId", 1);
        //when
        mockMvc.perform(post("/management/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.type", is("INVALID_PROPERTY")))
                .andExpect(jsonPath("$.message", is("Name should not be blank")));
    }

    @Test
    public void createArtistWithTooLongDescription() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        String description = "A".repeat(1001);
        jsonObject.put("name", "Jake");
        jsonObject.put("description", description);
        jsonObject.put("photoId", 1);
        //when
        mockMvc.perform(post("/management/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.type", is("INVALID_PROPERTY")))
                .andExpect(jsonPath("$.message", is("Description should be less than 1000 characters")));
    }

}
