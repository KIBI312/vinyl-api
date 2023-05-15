package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.GenreRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import org.springframework.stereotype.Service;

@Service
public class ManagementService {

    private final GenreRepository genreRepository;
    private final VinylRepository vinylRepository;

    public ManagementService(GenreRepository genreRepository, VinylRepository vinylRepository) {
        this.genreRepository = genreRepository;
        this.vinylRepository = vinylRepository;
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
