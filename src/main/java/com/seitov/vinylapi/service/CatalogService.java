package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ArtistDto;
import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.projection.ArtistDetails;
import com.seitov.vinylapi.projection.VinylDetails;
import com.seitov.vinylapi.projection.VinylLight;
import com.seitov.vinylapi.repository.ArtistRepository;
import com.seitov.vinylapi.repository.FormatRepository;
import com.seitov.vinylapi.repository.GenreRepository;
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
    private final FormatRepository formatRepository;
    private final GenreRepository genreRepository;
    private final ArtistRepository artistRepository;
    private final MapperFacade orikaMapper;

    public CatalogService(VinylRepository vinylRepository, FormatRepository formatRepository,
                          GenreRepository genreRepository, ArtistRepository artistRepository,
                          MapperFacade orikaMapper) {
        this.vinylRepository = vinylRepository;
        this.formatRepository = formatRepository;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<VinylLightDto> getVinylsLight(Integer page) {
        Pageable pageable = PageRequest.of(page, 50);
        List<VinylLight> vinylLights = vinylRepository.findAllProjectedBy(pageable, VinylLight.class);
        return orikaMapper.mapAsList(vinylLights, VinylLightDto.class);
    }

    public VinylDto getVinylById(Long id) {
        Optional<VinylDetails> vinylDetails = vinylRepository.readById(id, VinylDetails.class);
        if(vinylDetails.isEmpty()) {
            throw new ResourceNotFoundException("Vinyl with this id doesn't exist");
        }
        return orikaMapper.map(vinylDetails.get(), VinylDto.class);
    }

    public List<VinylLightDto> getVinylsLightByArtist(Long id) {
        return orikaMapper.mapAsList(vinylRepository.readByArtists_Id(id, VinylLight.class), VinylLightDto.class);
    }

    public List<VinylLightDto> getVinylsLightByGenre(Long id) {
        return orikaMapper.mapAsList(vinylRepository.readByGenres_Id(id, VinylLight.class), VinylLightDto.class);
    }

    public List<VinylLightDto> getVinylsLightByFormat(Long id) {
        return orikaMapper.mapAsList(vinylRepository.readByFormat_Id(id, VinylLight.class), VinylLightDto.class);
    }

    public List<Format> getFormats() {
        return formatRepository.findAll();
    }

    public List<ArtistDto> getArtists(Integer page) {
        Pageable pageable = PageRequest.of(page, 50);
        return orikaMapper.mapAsList(artistRepository.findAllProjectedBy(pageable, ArtistDetails.class), ArtistDto.class);
    }




}
