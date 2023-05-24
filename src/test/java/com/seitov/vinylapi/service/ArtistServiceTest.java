package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Artist;
import com.seitov.vinylapi.entity.Image;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.repository.ArtistRepository;
import com.seitov.vinylapi.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ArtistService artistService;

    @Test
    public void artistCreation() {
        //given
        ArtistDto artistDto = new ArtistDto(null, "Jake", "SomeArtist", 1L);
        Image image = new Image();
        image.setId(1L);
        Artist toCreate = new Artist(null, "Jake", "SomeArtist", image);
        Artist created = new Artist(0L, "Jake", "SomeArtist", image);
        ResourceId createdId = new ResourceId(0L);
        //when
        when(imageRepository.existsById(1L)).thenReturn(true);
        when(artistRepository.existsByName("Jake")).thenReturn(false);
        when(imageRepository.getReferenceById(1L)).thenReturn(image);
        when(artistRepository.save(toCreate)).thenReturn(created);
        //then
        assertEquals(createdId, artistService.createArtist(artistDto));
    }

    @Test
    public void artistCreationWithRedundantId() {
        //given
        ArtistDto artistDto = new ArtistDto(0L, "Jake", "SomeArtist", 1L);
        //then
        Exception ex = assertThrows(RedundantPropertyException.class,
                () -> artistService.createArtist(artistDto));
        assertEquals("Id not allowed in POST requests", ex.getMessage());
    }

    @Test
    public void artistCreationWithNonExistingPhoto() {
        //given
        ArtistDto artistDto = new ArtistDto(null, "Jake", "SomeArtist", 1L);
        //when
        when(imageRepository.existsById(1L)).thenReturn(false);
        //then
        Exception ex = assertThrows(DataConstraintViolationException.class,
                () -> artistService.createArtist(artistDto));
        assertEquals("Photo with provided id doesn't exist", ex.getMessage());
    }

    @Test
    public void artistCreationWithDuplicateName() {
        //given
        ArtistDto artistDto = new ArtistDto(null, "Jake", "SomeArtist", 1L);
        //when
        when(imageRepository.existsById(1L)).thenReturn(true);
        when(artistRepository.existsByName("Jake")).thenReturn(true);
        //then
        Exception ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> artistService.createArtist(artistDto));
        assertEquals("Artist with this name already exists", ex.getMessage());
    }

}
