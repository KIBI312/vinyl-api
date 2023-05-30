package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.entity.*;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.VinylRepository;
import com.seitov.vinylapi.repository.VinylShortRepository;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VinylServiceTest {

    @Mock
    private VinylRepository vinylRepository;
    @Mock
    VinylShortRepository vinylShortRepository;
    @Mock
    private MapperFacade orikaMapper;

    @InjectMocks
    private VinylService vinylService;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Test
    public void testGetVinylById() {
        //given
        Vinyl vinyl = new Vinyl();
        ArtistShort artistShort = new ArtistShort(1L, "MichaelJ");
        Soundtrack soundtrack = new Soundtrack(1L, "Billy jeans");
        Format format = new Format(1L, "2LP");
        Genre genre = new Genre(1L, "Pop-Rock");
        vinyl.setId(0L);
        vinyl.setName("Moonwalk");
        vinyl.setDescription("Legendary album of legendary artist");
        vinyl.setPrice(20.99);
        vinyl.setArtists(List.of(artistShort));
        vinyl.setGenres(List.of(genre));
        vinyl.setFormat(format);
        vinyl.setInStock(true);
        vinyl.setRecordLabel("EMI");
        vinyl.setTrackList(List.of(soundtrack));
        vinyl.setPhotoId(1L);

        VinylDto vinylDto = new VinylDto();
        vinylDto.setId(0L);
        vinylDto.setName("Moonwalk");
        vinylDto.setDescription("Legendary album of legendary artist");
        vinylDto.setPrice(20.99);
        vinylDto.setArtists(List.of(artistShort));
        vinylDto.setGenres(List.of(genre));
        vinylDto.setFormat(format);
        vinylDto.setInStock(true);
        vinylDto.setRecordLabel("EMI");
        vinylDto.setTrackList(List.of(soundtrack));
        vinylDto.setPhotoId(1L);
        //when
        when(vinylRepository.findById(0L)).thenReturn(Optional.of(vinyl));
        when(orikaMapper.map(vinyl, VinylDto.class)).thenReturn(vinylDto);
        //then
        assertEquals(vinylDto, vinylService.getVinylById(0L));
    }

    @Test
    public void testGetVinylNonExisting() {
        //when
        when(vinylRepository.findById(0L)).thenReturn(Optional.ofNullable(null));
        //then
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> vinylService.getVinylById(0L));
        assertEquals("Vinyl with this id doesn't exist", ex.getMessage());
    }

    @Test
    public void testGetVinylShortList() {
        //given
        Pageable pageable = PageRequest.of(0, 50);
        List<VinylShort> vinylShorts = new ArrayList<>();
        for (long i = 0; i < 3; i++){
            vinylShorts.add(new VinylShort(i, "Vinyl #"+i,
                    null, null, null, null));
        }
        Page<VinylShort> page = new PageImpl<>(vinylShorts);
        //when
        when(vinylShortRepository.findAll(pageable)).thenReturn(page);
        //then
        assertEquals(3, vinylService.getVinylsShort(0).size());
        assertEquals(vinylShorts, vinylService.getVinylsShort(0));
    }

}
