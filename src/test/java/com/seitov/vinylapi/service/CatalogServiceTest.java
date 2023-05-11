package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.projection.*;
import com.seitov.vinylapi.repository.VinylRepository;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class CatalogServiceTest {

    @Mock
    private VinylRepository vinylRepository;
    @Mock
    private MapperFacade orikaMapper;

    @InjectMocks
    private CatalogService catalogService;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Test
    public void testGetVinylById() {
        //given
        VinylDetails vinylDetails = factory.createProjection(VinylDetails.class);
        ArtistName artistName = factory.createProjection(ArtistName.class);
        PhotoId photoId = factory.createProjection(PhotoId.class);
        SoundtrackName trackName = factory.createProjection(SoundtrackName.class);
        Format format = new Format(1L, "2LP");
        Genre genre = new Genre(1L, "Pop-Rock");
        artistName.setName("MichaelJ");
        trackName.setName("Billy jeans");
        photoId.setId(1L);
        vinylDetails.setId(0L);
        vinylDetails.setName("Moonwalk");
        vinylDetails.setDescription("Legendary album of legendary artist");
        vinylDetails.setPrice(20.99);
        vinylDetails.setArtists(List.of(artistName));
        vinylDetails.setGenres(List.of(genre));
        vinylDetails.setFormat(format);
        vinylDetails.setInStock(true);
        vinylDetails.setRecordLabel("EMI");
        vinylDetails.setTrackList(List.of(trackName));
        vinylDetails.setPhotoHighRes(photoId);

        VinylDto vinylDto = new VinylDto();
        vinylDto.setId(0L);
        vinylDto.setName("Moonwalk");
        vinylDto.setDescription("Legendary album of legendary artist");
        vinylDto.setPrice(20.99);
        vinylDto.setArtists(List.of(artistName));
        vinylDto.setGenres(List.of(genre));
        vinylDto.setFormat(format);
        vinylDto.setInStock(true);
        vinylDto.setRecordLabel("EMI");
        vinylDto.setTrackList(List.of("Billy jeans"));
        vinylDto.setPhotoId(1L);
        //when
        when(vinylRepository.readById(0L, VinylDetails.class)).thenReturn(Optional.of(vinylDetails));
        when(orikaMapper.map(vinylDetails, VinylDto.class)).thenReturn(vinylDto);
        //then
        assertEquals(vinylDto, catalogService.getVinylById(0L));
    }

    @Test
    public void testGetVinylNonExisting() {
        //when
        when(vinylRepository.readById(0L, VinylDetails.class)).thenReturn(Optional.ofNullable(null));
        //then
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> catalogService.getVinylById(0L));
        assertEquals("Vinyl with this id doesn't exist", ex.getMessage());
    }

    @Test
    public void testGetVinylLightList() {
        //given
        Pageable pageable = PageRequest.of(0, 50);
        List<VinylLight> vinylLights = new ArrayList<>();
        List<VinylLightDto> vinylLightDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            VinylLight vinylLight = factory.createProjection(VinylLight.class);
            vinylLight.setId((long) i);
            vinylLight.setName("Vinyl #"+i);
            vinylLights.add(vinylLight);
            VinylLightDto vinylLightDto = new VinylLightDto();
            vinylLightDto.setId((long) i);
            vinylLightDto.setName("Vinyl #"+i);
            vinylLightDtos.add(vinylLightDto);
        }
        //when
        when(vinylRepository.findAllProjectedBy(pageable, VinylLight.class)).thenReturn(vinylLights);
        when(orikaMapper.mapAsList(vinylLights, VinylLightDto.class)).thenReturn(vinylLightDtos);
        //then
        assertEquals(3, catalogService.getVinylsLight(0).size());
        assertEquals(vinylLightDtos, catalogService.getVinylsLight(0));
    }



}
