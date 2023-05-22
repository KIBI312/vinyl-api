package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Artist;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.entity.Image;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.ArtistRepository;
import com.seitov.vinylapi.repository.GenreRepository;
import com.seitov.vinylapi.repository.ImageRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagementServiceTest {

    @Mock
    private GenreRepository genreRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private VinylRepository vinylRepository;

    @InjectMocks
    private ManagementService managementService;

    @Test
    public void genreCreationTest() {
        //given
        Genre genre = new Genre();
        genre.setName("Rock");
        ResourceId resourceId = new ResourceId(1L);
        //when
        when(genreRepository.existsByName("Rock")).thenReturn(false);
        when(genreRepository.save(genre)).thenReturn(new Genre(1L, "Rock"));
        //then
        assertEquals(resourceId, managementService.createGenre(genre));
    }

    @Test
    public void genreDuplicateCreationTest() {
        //given
        Genre genre = new Genre();
        genre.setName("Rock");
        //when
        when(genreRepository.existsByName("Rock")).thenReturn(true);
        //then
        Exception ex = assertThrows(ResourceAlreadyExistsException.class, () -> managementService.createGenre(genre));
        assertEquals("This genre already exists", ex.getMessage());
    }

    @Test
    public void genreRedundantIdCreationTest() {
        //given
        Genre genre = new Genre(1L, "Rock");
        //then
        Exception ex = assertThrows(RedundantPropertyException.class, () -> managementService.createGenre(genre));
        assertEquals("Id not allowed in POST requests", ex.getMessage());
    }

    @Test
    public void nonExistingGenreDeletionTest() {
        //given
        ResourceId genreId = new ResourceId(1L);
        //when
        when(genreRepository.existsById(1L)).thenReturn(false);
        //then
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> managementService.deleteGenre(genreId));
        assertEquals("Genre with this id doesn't exist", ex.getMessage());
    }

    @Test
    public void busyGenreDeletionTest() {
        //given
        ResourceId genreId = new ResourceId(1L);
        //when
        when(genreRepository.existsById(1L)).thenReturn(true);
        when(vinylRepository.existsByGenres_Id(1L)).thenReturn(true);
        //then
        Exception ex = assertThrows(DataConstraintViolationException.class, () -> managementService.deleteGenre(genreId));
        assertEquals("Cannot delete while there's vinyls dependent from this genre!", ex.getMessage());
    }

    @Test
    public void genreDeletionTest() {
        //given
        ResourceId genreId = new ResourceId(1L);
        //when
        when(genreRepository.existsById(1L)).thenReturn(true);
        when(vinylRepository.existsByGenres_Id(1L)).thenReturn(false);
        //then
        assertDoesNotThrow(() -> managementService.deleteGenre(genreId));
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
        assertEquals(new ResourceId(1L), managementService.createPhoto(file));
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
        Exception ex = assertThrows(RuntimeException.class, () -> managementService.createPhoto(file));
        assertEquals("Error occurred during image processing", ex.getMessage());
    }

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
        assertEquals(createdId, managementService.createArtist(artistDto));
    }

    @Test
    public void artistCreationWithRedundantId() {
        //given
        ArtistDto artistDto = new ArtistDto(0L, "Jake", "SomeArtist", 1L);
        //then
        Exception ex = assertThrows(RedundantPropertyException.class,
                () -> managementService.createArtist(artistDto));
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
                () -> managementService.createArtist(artistDto));
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
                () -> managementService.createArtist(artistDto));
        assertEquals("Artist with this name already exists", ex.getMessage());
    }

}
