package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Artist;
import com.seitov.vinylapi.entity.VinylShort;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.ArtistRepository;
import com.seitov.vinylapi.repository.ImageRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import com.seitov.vinylapi.repository.VinylShortRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final VinylRepository vinylRepository;
    private final VinylShortRepository vinylShortRepository;
    private final ImageRepository imageRepository;
    private final MapperFacade orikaMapper;

    public ArtistService(ArtistRepository artistRepository, VinylRepository vinylRepository,
                         VinylShortRepository vinylShortRepository, ImageRepository imageRepository,
                         MapperFacade orikaMapper) {
        this.artistRepository = artistRepository;
        this.vinylRepository = vinylRepository;
        this.vinylShortRepository = vinylShortRepository;
        this.imageRepository = imageRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<ArtistDto> getArtists(Integer page) {
        Pageable pageable = PageRequest.of(page, 50);
        return orikaMapper.mapAsList(artistRepository.findAll(pageable),ArtistDto.class);
    }

    public List<VinylShort> getVinylsShortByArtist(Long id) {
        return vinylShortRepository.findAllByArtists_Id(id);
    }

    public ResourceId createArtist(ArtistDto artistDto) {
        if(artistDto.getId()!=null) {
            throw new RedundantPropertyException("Id not allowed in POST requests");
        }
        if(!imageRepository.existsById(artistDto.getPhotoId())) {
            throw new DataConstraintViolationException("Photo with provided id doesn't exist");
        }
        if(artistRepository.existsByName(artistDto.getName())) {
            throw new ResourceAlreadyExistsException("Artist with this name already exists");
        }
        Artist artist = orikaMapper.map(artistDto, Artist.class);
        artist.setPhoto(imageRepository.getReferenceById(artistDto.getPhotoId()));
        Long id = artistRepository.save(artist).getId();
        return new ResourceId(id);
    }

    public void deleteArtist(Long id) {
        if(!artistRepository.existsById(id)) {
            throw new ResourceNotFoundException("Artist with this id doesn't exist");
        }
        if(vinylRepository.existsByArtists_Id(id)) {
            throw new DataConstraintViolationException("Cannot delete while there's vinyls dependent from this artist!");
        }
        artistRepository.deleteById(id);
    }

}
