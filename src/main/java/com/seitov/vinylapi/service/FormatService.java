package com.seitov.vinylapi.service;

import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.entity.VinylShort;
import com.seitov.vinylapi.repository.FormatRepository;
import com.seitov.vinylapi.repository.VinylShortRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatService {

    private final FormatRepository formatRepository;
    private final VinylShortRepository vinylShortRepository;
    private final MapperFacade orikaMapper;

    public FormatService(FormatRepository formatRepository, VinylShortRepository vinylShortRepository, MapperFacade orikaMapper) {
        this.formatRepository = formatRepository;
        this.vinylShortRepository = vinylShortRepository;
        this.orikaMapper = orikaMapper;
    }

    public List<Format> getFormats() {
        return formatRepository.findAll();
    }

    public List<VinylShort> getVinylsShortByFormat(Long id) {
        return vinylShortRepository.findAllByFormat_Id(id);
    }

}
