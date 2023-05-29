package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.VinylDto;
import com.seitov.vinylapi.entity.VinylShort;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.projection.VinylDetails;
import com.seitov.vinylapi.repository.VinylRepository;
import com.seitov.vinylapi.repository.VinylShortRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VinylService {

    private final VinylRepository vinylRepository;
    private final VinylShortRepository vinylShortRepository;
    private final MapperFacade orikaMapper;

    public VinylService(VinylRepository vinylRepository, VinylShortRepository vinylShortRepository,
                        MapperFacade orikaMapper) {
        this.vinylRepository = vinylRepository;
        this.vinylShortRepository = vinylShortRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<VinylShort> getVinylsShort(Integer page) {
        Pageable pageable = PageRequest.of(page, 50);
        return vinylShortRepository.findAll(pageable).getContent();
    }

    public VinylDto getVinylById(Long id) {
        Optional<VinylDetails> vinylDetails = vinylRepository.readById(id, VinylDetails.class);
        if(vinylDetails.isEmpty()) {
            throw new ResourceNotFoundException("Vinyl with this id doesn't exist");
        }
        return orikaMapper.map(vinylDetails.get(), VinylDto.class);
    }

}
