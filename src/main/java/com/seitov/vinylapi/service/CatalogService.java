package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Image;
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
import java.util.Optional;

@Service
public class CatalogService {

    private final VinylRepository vinylRepository;
    private final ArtistRepository artistRepository;
    private final ImageRepository imageRepository;
    private final MapperFacade orikaMapper;

    public CatalogService(VinylRepository vinylRepository, ArtistRepository artistRepository,
                          MapperFacade orikaMapper, ImageRepository imageRepository) {
        this.vinylRepository = vinylRepository;
        this.artistRepository = artistRepository;
        this.orikaMapper = orikaMapper;
        this.imageRepository = imageRepository;
    }

    public List<VinylLightDto> getVinylsLightByArtist(Long id) {
        return orikaMapper.mapAsList(vinylRepository.readByArtists_Id(id, VinylLight.class), VinylLightDto.class);
    }

    public List<ArtistDto> getArtists(Integer page) {
        Pageable pageable = PageRequest.of(page, 50);
        return orikaMapper.mapAsList(artistRepository.findAllProjectedBy(pageable, ArtistDetails.class), ArtistDto.class);
    }

    public byte[] getPhoto(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if(image.isEmpty()) {
            throw new ResourceNotFoundException("Photo with this id doesn't exist");
        }
        return image.get().getContent();
    }

}
