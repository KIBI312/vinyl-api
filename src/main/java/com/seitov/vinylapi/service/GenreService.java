package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
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

    public ResourceId createGenre(Genre genre) {
        if(genre.getId()!=null) {
            throw new RedundantPropertyException("Id not allowed in POST requests");
        }
        if(genreRepository.existsByName(genre.getName())) {
            throw new ResourceAlreadyExistsException("This genre already exists");
        }
        Long id = genreRepository.save(genre).getId();
        return new ResourceId(id);
    }

    public void deleteGenre(ResourceId genreId) {
        if(!genreRepository.existsById(genreId.getId())) {
            throw new ResourceNotFoundException("Genre with this id doesn't exist");
        }
        if(vinylRepository.existsByGenres_Id(genreId.getId())) {
            throw new DataConstraintViolationException("Cannot delete while there's vinyls dependent from this genre!");
        }
        genreRepository.deleteById(genreId.getId());
    }

}
