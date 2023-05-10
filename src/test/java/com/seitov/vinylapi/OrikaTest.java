package com.seitov.vinylapi;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.projection.*;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrikaTest {

    @SpyBean
    private MapperFacade orikaMapper;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Test
    public void testVinylDetailsToVinylDto() {
        //given
        VinylDetails vinylDetails = factory.createProjection(VinylDetails.class);
        ArtistName artistName = factory.createProjection(ArtistName.class);
        FormatName formatName = factory.createProjection(FormatName.class);
        GenreName genreName = factory.createProjection(GenreName.class);
        PhotoId photoId = factory.createProjection(PhotoId.class);
        SoundtrackName trackName = factory.createProjection(SoundtrackName.class);
        artistName.setName("MichaelJ");
        formatName.setName("2LP");
        genreName.setName("Pop");
        trackName.setName("Billy jeans");
        photoId.setId(1L);
        vinylDetails.setId(0L);
        vinylDetails.setName("Moonwalk");
        vinylDetails.setDescription("Legendary album of legendary artist");
        vinylDetails.setPrice(20.99);
        vinylDetails.setArtists(List.of(artistName));
        vinylDetails.setGenres(List.of(genreName));
        vinylDetails.setFormat(formatName);
        vinylDetails.setInStock(true);
        vinylDetails.setRecordLabel("EMI");
        vinylDetails.setTrackList(List.of(trackName));
        vinylDetails.setPhotoHighRes(photoId);

        VinylDto vinylDto = new VinylDto();
        vinylDto.setId(0L);
        vinylDto.setName("Moonwalk");
        vinylDto.setDescription("Legendary album of legendary artist");
        vinylDto.setPrice(20.99);
        vinylDto.setArtists(List.of("MichaelJ"));
        vinylDto.setGenres(List.of("Pop"));
        vinylDto.setFormat("2LP");
        vinylDto.setInStock(true);
        vinylDto.setRecordLabel("EMI");
        vinylDto.setTrackList(List.of("Billy jeans"));
        vinylDto.setPhotoId(1L);
        //then
        assertEquals(vinylDto, orikaMapper.map(vinylDetails, VinylDto.class));
    }

    @Test
    public void testVinylLightToVinylLightDto() {
        //given
        VinylLight vinylLight = factory.createProjection(VinylLight.class);
        FormatName formatName = factory.createProjection(FormatName.class);
        ArtistName artistName = factory.createProjection(ArtistName.class);
        PhotoId photoId = factory.createProjection(PhotoId.class);
        formatName.setName("2LP");
        artistName.setName("MichaelJ");
        photoId.setId(1L);
        vinylLight.setId(0L);
        vinylLight.setName("Moonwalk");
        vinylLight.setPrice(20.99);
        vinylLight.setFormat(formatName);
        vinylLight.setArtists(List.of(artistName));
        vinylLight.setPhotoLowRes(photoId);

        VinylLightDto vinylLightDto = new VinylLightDto();
        vinylLightDto.setId(0L);
        vinylLightDto.setName("Moonwalk");
        vinylLightDto.setPrice(20.99);
        vinylLightDto.setFormat("2LP");
        vinylLightDto.setArtist(List.of("MichaelJ"));
        vinylLightDto.setPhotoId(1L);
        //then
        assertEquals(vinylLightDto, orikaMapper.map(vinylLight, VinylLightDto.class));
    }

    @Test
    public void testArtistDetailsToArtistDto() {
        //given
        ArtistDetails artistDetails = factory.createProjection(ArtistDetails.class);
        PhotoId photoId = factory.createProjection(PhotoId.class);
        photoId.setId(1L);
        artistDetails.setId(0L);
        artistDetails.setName("MichaelJ");
        artistDetails.setDescription("Legendary legend of pop and rock");
        artistDetails.setPhoto(photoId);

        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(0L);
        artistDto.setName("MichaelJ");
        artistDto.setDescription("Legendary legend of pop and rock");
        artistDto.setPhotoId(1L);
        //then
        assertEquals(artistDto, orikaMapper.map(artistDetails, ArtistDto.class));

    }




}
