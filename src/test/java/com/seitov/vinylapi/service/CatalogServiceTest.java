package com.seitov.vinylapi.service;

import com.seitov.vinylapi.entity.Image;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.ImageRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

    @Mock
    private VinylRepository vinylRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private MapperFacade orikaMapper;

    @InjectMocks
    private CatalogService catalogService;

    @Test
    public void photoRetrieval() {
        //given
        byte[] content = new byte[100];
        new Random().nextBytes(content);
        Image image = new Image(1L, content);
        //when
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        //then
        assertEquals(content, catalogService.getPhoto(1L));
    }

    @Test
    public void nonExistingPhotoRetrieval() {
        //when
        when(imageRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        //then
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> catalogService.getPhoto(1L));
        assertEquals("Photo with this id doesn't exist", ex.getMessage());
    }

}
