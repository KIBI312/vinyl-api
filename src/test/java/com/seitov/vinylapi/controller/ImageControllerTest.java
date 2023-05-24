package com.seitov.vinylapi.controller;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    public void photoUpload() throws Exception {
        //given
        byte[] content = new byte[100];
        new Random().nextBytes(content);
        MockMultipartFile file = new MockMultipartFile("photo", content);
        //when
        when(imageService.createPhoto(file)).thenReturn(new ResourceId(1L));
        //then
        mockMvc.perform(multipart("/photo").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void uploadCorruptedPhoto() throws Exception {
        //given
        byte[] content = new byte[100];
        new Random().nextBytes(content);
        MockMultipartFile file = new MockMultipartFile("photo", content);
        //when
        when(imageService.createPhoto(file)).thenThrow(new RuntimeException("Error occurred during image processing"));
        //then
        mockMvc.perform(multipart("/photo").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code", is(500)))
                .andExpect(jsonPath("$.type", is("SERVER_ERROR")))
                .andExpect(jsonPath("$.message", is("Error occurred during image processing")));
    }

}
