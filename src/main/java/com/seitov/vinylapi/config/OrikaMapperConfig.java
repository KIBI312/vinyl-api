package com.seitov.vinylapi.config;

import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.projection.VinylLight;
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
                                .map(VinylLight.ArtistName::getName)
                                .collect(Collectors.toList()));
                        vinylLightDto.setFormat(vinylLight.getFormat().getName());
                        vinylLightDto.setPhotoId(vinylLight.getPhotoLowRes().getId());
                    }
                }).byDefault().register();
        return mapperFactory.getMapperFacade();
    }

}
