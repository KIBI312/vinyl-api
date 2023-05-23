package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.projection.VinylLight;
import com.seitov.vinylapi.repository.GenreRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final VinylRepository vinylRepository;
    private final MapperFacade orikaMapper;

    public GenreService(GenreRepository genreRepository, VinylRepository vinylRepository, MapperFacade orikaMapper) {
        this.genreRepository = genreRepository;
        this.vinylRepository = vinylRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public List<VinylLightDto> getVinylsLightByGenre(Long id) {
        return orikaMapper.mapAsList(vinylRepository.readByGenres_Id(id, VinylLight.class), VinylLightDto.class);
    }

}
