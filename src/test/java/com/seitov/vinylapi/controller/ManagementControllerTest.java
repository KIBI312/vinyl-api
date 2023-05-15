package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
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
import static org.mockito.Mockito.when;
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



}
