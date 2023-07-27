package com.seitov.vinylapi;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.entity.*;
import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrikaTest {

    @SpyBean
    private MapperFacade orikaMapper;

    @Test
    public void testVinylDetailsToVinylDto() {
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
        //then
        assertEquals(vinylDto, orikaMapper.map(vinyl, VinylDto.class));
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
