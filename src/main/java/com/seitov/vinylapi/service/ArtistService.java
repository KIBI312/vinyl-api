package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Artist;
import com.seitov.vinylapi.exception.DataConstraintViolationException;
import com.seitov.vinylapi.exception.RedundantPropertyException;
import com.seitov.vinylapi.exception.ResourceAlreadyExistsException;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.projection.ArtistDetails;
import com.seitov.vinylapi.projection.VinylLight;
import com.seitov.vinylapi.repository.ArtistRepository;
import com.seitov.vinylapi.repository.ImageRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final VinylRepository vinylRepository;
    private final ImageRepository imageRepository;
    private final MapperFacade orikaMapper;

    public ArtistService(ArtistRepository artistRepository, VinylRepository vinylRepository,
                         ImageRepository imageRepository, MapperFacade orikaMapper) {
        this.artistRepository = artistRepository;
        this.vinylRepository = vinylRepository;
        this.imageRepository = imageRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<ArtistDto> getArtists(Integer page) {
        Pageable pageable = PageRequest.of(page, 50);
        return orikaMapper.mapAsList(artistRepository.findAllProjectedBy(pageable, ArtistDetails.class), ArtistDto.class);
    }

    public List<VinylLightDto> getVinylsLightByArtist(Long id) {
        return orikaMapper.mapAsList(vinylRepository.readByArtists_Id(id, VinylLight.class), VinylLightDto.class);
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
        Artist artist = new Artist();
        artist.setName(artistDto.getName());
        artist.setDescription(artistDto.getDescription());
        artist.setPhoto(imageRepository.getReferenceById(artistDto.getPhotoId()));
        Long id = artistRepository.save(artist).getId();
        return new ResourceId(id);
    }

    public void deleteArtist(ResourceId resourceId) {
        if(!artistRepository.existsById(resourceId.getId())) {
            throw new ResourceNotFoundException("Artist with this id doesn't exist");
        }
        if(vinylRepository.existsByArtists_Id(resourceId.getId())) {
            throw new DataConstraintViolationException("Cannot delete while there's vinyls dependent from this artist!");
        }
        artistRepository.deleteById(resourceId.getId());
    }

}
