package com.seitov.vinylapi;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.entity.*;
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
        ArtistShort artistName = new ArtistShort(1L, "MichaelJ");
        PhotoId photoId = factory.createProjection(PhotoId.class);
        SoundtrackName trackName = factory.createProjection(SoundtrackName.class);
        Format format = new Format(1L, "2LP");
        Genre genre = new Genre(1L, "Pop-Rock");
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
        //then
        assertEquals(vinylDto, orikaMapper.map(vinylDetails, VinylDto.class));
    }

    @Test
    public void testArtistToArtistDto() {
        //given
        Artist artist = new Artist();
        artist.setId(0L);
        artist.setName("MichaelJ");
        artist.setDescription("Legendary legend of pop and rock");
        artist.setPhoto(new Image(1L, null));
        artist.setPhotoId(1L);

        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(0L);
        artistDto.setName("MichaelJ");
        artistDto.setDescription("Legendary legend of pop and rock");
        artistDto.setPhotoId(1L);
        //then
        assertEquals(artistDto, orikaMapper.map(artist, ArtistDto.class));
    }

    @Test
    public void testArtistDtoToArtist() {
        //given
        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(0L);
        artistDto.setName("MichaelJ");
        artistDto.setDescription("Legendary legend of pop and rock");
        artistDto.setPhotoId(1L);

        Artist artist = new Artist();
        artist.setId(0L);
        artist.setName("MichaelJ");
        artist.setDescription("Legendary legend of pop and rock");
        artist.setPhotoId(1L);
        //then
        assertEquals(artist, orikaMapper.map(artistDto, Artist.class));
    }


}
