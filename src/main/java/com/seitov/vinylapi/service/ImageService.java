package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Image;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public byte[] getPhoto(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if(image.isEmpty()) {
            throw new ResourceNotFoundException("Photo with this id doesn't exist");
        }
        return image.get().getContent();
    }

    public ResourceId createPhoto(MultipartFile imageContent) {
        Image image = new Image();
        try {
            image.setContent(imageContent.getBytes());
        } catch (IOException ex) {
            throw new RuntimeException("Error occurred during image processing");
        }
        Long id = imageRepository.save(image).getId();
        return new ResourceId(id);
    }

}
