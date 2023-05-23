package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.projection.ArtistDetails;
import com.seitov.vinylapi.projection.VinylLight;
import com.seitov.vinylapi.repository.ArtistRepository;
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
    private final MapperFacade orikaMapper;

    public ArtistService(ArtistRepository artistRepository, VinylRepository vinylRepository, MapperFacade orikaMapper) {
        this.artistRepository = artistRepository;
        this.vinylRepository = vinylRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<ArtistDto> getArtists(Integer page) {
        Pageable pageable = PageRequest.of(page, 50);
        return orikaMapper.mapAsList(artistRepository.findAllProjectedBy(pageable, ArtistDetails.class), ArtistDto.class);
    }

    public List<VinylLightDto> getVinylsLightByArtist(Long id) {
        return orikaMapper.mapAsList(vinylRepository.readByArtists_Id(id, VinylLight.class), VinylLightDto.class);
    }

}
