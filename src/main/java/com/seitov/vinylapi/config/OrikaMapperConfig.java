package com.seitov.vinylapi.config;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.entity.Artist;
import com.seitov.vinylapi.entity.Vinyl;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaMapperConfig {

    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @Bean
    public MapperFacade orikaMapper() {
        mapperFactory.classMap(Vinyl.class, VinylDto.class)
                .exclude("photoLowRes")
                .exclude("photoHighRes")
                .byDefault().register();
        mapperFactory.classMap(Artist.class, ArtistDto.class)
                .exclude("photo")
                .byDefault().register();
        return mapperFactory.getMapperFacade();
    }

}
