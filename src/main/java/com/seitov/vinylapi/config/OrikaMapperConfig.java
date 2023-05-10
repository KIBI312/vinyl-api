package com.seitov.vinylapi.config;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.projection.*;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
public class OrikaMapperConfig {

    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Bean
    public MapperFacade orikaMapper() {
        mapperFactory.classMap(VinylLight.class, VinylLightDto.class)
                .customize(new CustomMapper<VinylLight, VinylLightDto>() {
                    @Override
                    public void mapAtoB(VinylLight vinylLight, VinylLightDto vinylLightDto, MappingContext context) {
                        vinylLightDto.setId(vinylLight.getId());
                        vinylLightDto.setName(vinylLight.getName());
                        vinylLightDto.setPrice(vinylLight.getPrice());
                        vinylLightDto.setArtist(vinylLight.getArtists().stream()
                                .map(ArtistName::getName)
                                .collect(Collectors.toList()));
                        vinylLightDto.setFormat(vinylLight.getFormat().getName());
                        vinylLightDto.setPhotoId(vinylLight.getPhotoLowRes().getId());
                    }
                }).register();
        mapperFactory.classMap(VinylDetails.class, VinylDto.class)
                .customize(new CustomMapper<VinylDetails, VinylDto>() {
                    @Override
                    public void mapAtoB(VinylDetails vinylDetails, VinylDto vinylDto, MappingContext context) {
                        vinylDto.setId(vinylDetails.getId());
                        vinylDto.setName(vinylDetails.getName());
                        vinylDto.setDescription(vinylDetails.getDescription());
                        vinylDto.setPrice(vinylDetails.getPrice());
                        vinylDto.setArtists(vinylDetails.getArtists().stream()
                                .map(ArtistName::getName)
                                .collect(Collectors.toList()));
                        vinylDto.setGenres(vinylDetails.getGenres().stream()
                                .map(GenreName::getName)
                                .collect(Collectors.toList()));
                        vinylDto.setFormat(vinylDetails.getFormat().getName());
                        vinylDto.setInStock(vinylDetails.getInStock());
                        vinylDto.setRecordLabel(vinylDetails.getRecordLabel());
                        vinylDto.setTrackList(vinylDetails.getTrackList().stream()
                                .map(SoundtrackName::getName)
                                .collect(Collectors.toList()));
                        vinylDto.setPhotoId(vinylDetails.getPhotoHighRes().getId());
                    }
                }).register();
        mapperFactory.classMap(ArtistDetails.class, ArtistDto.class).customize(new CustomMapper<ArtistDetails, ArtistDto>() {
            @Override
            public void mapAtoB(ArtistDetails artistDetails, ArtistDto artistDto, MappingContext context) {
                artistDto.setId(artistDetails.getId());
                artistDto.setName(artistDetails.getName());
                artistDto.setDescription(artistDetails.getDescription());
                artistDto.setPhotoId(artistDetails.getPhoto().getId());
            }
        }).register();
        return mapperFactory.getMapperFacade();
    }

}
