package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Image;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    public void photoRetrieval() {
        //given
        byte[] content = new byte[100];
        new Random().nextBytes(content);
        Image image = new Image(1L, content);
        //when
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        //then
        assertEquals(content, imageService.getPhoto(1L));
    }

    @Test
    public void nonExistingPhotoRetrieval() {
        //when
        when(imageRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        //then
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> imageService.getPhoto(1L));
        assertEquals("Photo with this id doesn't exist", ex.getMessage());
    }

    @Test
    public void photoCreationTest() {
        //given
        byte[] content = new byte[100];
        new Random().nextBytes(content);
        MultipartFile file = new MockMultipartFile("photo", content);
        Image image = new Image();
        image.setContent(content);
        //when
        when(imageRepository.save(image)).thenReturn(new Image(1L, content));
        //then
        assertEquals(new ResourceId(1L), imageService.createPhoto(file));
    }

    @Test
    public void photoCreationTestError() {
        //given
        byte[] content = new byte[100];
        new Random().nextBytes(content);
        MultipartFile file = new MockMultipartFile("photo", content) {
            @Override
            public byte[] getBytes() throws IOException {
                throw new IOException();
            }
        };
        //then
        Exception ex = assertThrows(RuntimeException.class, () -> imageService.createPhoto(file));
        assertEquals("Error occurred during image processing", ex.getMessage());
    }

    @Test
    public void nonExistingPhotoDeletionTest() {
        //given
        Long id = 1L;
        //when
        when(imageRepository.existsById(id)).thenReturn(false);
        //then
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> imageService.deletePhoto(1L));
        assertEquals("Photo with this id doesn't exist", ex.getMessage());
    }

}
