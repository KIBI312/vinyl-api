package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.DatabaseContainer;
import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.service.ArtistService;
import org.json.JSONObject;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtistControllerTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = DatabaseContainer.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

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
        when(artistService.createArtist(artistDto)).thenReturn(result);
        //then
        mockMvc.perform(post("/artists")
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
        mockMvc.perform(post("/artists")
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
        mockMvc.perform(post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.type", is("INVALID_PROPERTY")))
                .andExpect(jsonPath("$.message", is("Description should be less than 1000 characters")));
    }

}
