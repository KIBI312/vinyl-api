package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManagementServiceTest {

    @Mock
    private GenreRepository genreRepository;

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
    public void genreRedundantIdTest() {
        //given
        Genre genre = new Genre(1L, "Rock");
        //then
        Exception ex = assertThrows(RedundantPropertyException.class, () -> managementService.createGenre(genre));
        assertEquals("Id not allowed in POST requests", ex.getMessage());
    }


}
