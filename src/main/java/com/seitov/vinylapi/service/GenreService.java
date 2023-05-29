package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.entity.VinylShort;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.GenreRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import com.seitov.vinylapi.repository.VinylShortRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final VinylRepository vinylRepository;
    private final VinylShortRepository vinylShortRepository;
    private final MapperFacade orikaMapper;

    public GenreService(GenreRepository genreRepository, VinylRepository vinylRepository,
                        VinylShortRepository vinylShortRepository, MapperFacade orikaMapper) {
        this.genreRepository = genreRepository;
        this.vinylRepository = vinylRepository;
        this.vinylShortRepository = vinylShortRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public List<VinylShort> getVinylsShortByGenre(Long id) {
        return vinylShortRepository.findAllByGenres_Id(id);
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

    public void deleteGenre(Long id) {
        if(!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre with this id doesn't exist");
        }
        if(vinylRepository.existsByGenres_Id(id)) {
            throw new DataConstraintViolationException("Cannot delete while there's vinyls dependent from this genre!");
        }
        genreRepository.deleteById(id);
    }

}
