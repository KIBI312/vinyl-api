package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Genre;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.repository.GenreRepository;
import org.springframework.stereotype.Service;

@Service
public class ManagementService {

    private final GenreRepository genreRepository;

    public ManagementService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
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

}
