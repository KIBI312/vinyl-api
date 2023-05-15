package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.GenreRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagementServiceTest {

    @Mock
    private GenreRepository genreRepository;
    @Mock
    VinylRepository vinylRepository;

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

}
