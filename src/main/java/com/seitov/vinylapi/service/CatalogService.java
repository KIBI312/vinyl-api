package com.seitov.vinylapi.service;

import com.seitov.vinylapi.entity.Image;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatalogService {
    private final ImageRepository imageRepository;

    public CatalogService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public byte[] getPhoto(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if(image.isEmpty()) {
            throw new ResourceNotFoundException("Photo with this id doesn't exist");
        }
        return image.get().getContent();
    }

}
