package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.VinylLightDto;
import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.projection.VinylLight;
import com.seitov.vinylapi.repository.FormatRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatService {

    private final FormatRepository formatRepository;
    private final VinylRepository vinylRepository;
    private final MapperFacade orikaMapper;

    public FormatService(FormatRepository formatRepository, VinylRepository vinylRepository, MapperFacade orikaMapper) {
        this.formatRepository = formatRepository;
        this.vinylRepository = vinylRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<Format> getFormats() {
        return formatRepository.findAll();
    }

    public List<VinylLightDto> getVinylsLightByFormat(Long id) {
        return orikaMapper.mapAsList(vinylRepository.readByFormat_Id(id, VinylLight.class), VinylLightDto.class);
    }

}
