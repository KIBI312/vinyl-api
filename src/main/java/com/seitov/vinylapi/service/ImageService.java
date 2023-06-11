package com.seitov.vinylapi.service;

import com.seitov.vinylapi.dto.ResourceId;
import com.seitov.vinylapi.entity.Artist;
import com.seitov.vinylapi.entity.Image;
import com.seitov.vinylapi.entity.Vinyl;
import com.seitov.vinylapi.exception.ResourceNotFoundException;
import com.seitov.vinylapi.repository.ArtistRepository;
import com.seitov.vinylapi.repository.ImageRepository;
import com.seitov.vinylapi.repository.VinylRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ArtistRepository artistRepository;
    private final VinylRepository vinylRepository;

    public ImageService(ImageRepository imageRepository, ArtistRepository artistRepository,
                        VinylRepository vinylRepository) {
        this.imageRepository = imageRepository;
        this.artistRepository = artistRepository;
        this.vinylRepository = vinylRepository;
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

    public void deletePhoto(Long id) {
        if(!imageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Photo with this id doesn't exist");
        }
        List<Artist> artists = artistRepository.findByPhotoId(id);
        List<Vinyl> vinylsWithHighPhoto = vinylRepository.findByPhotoHighRes_Id(id);
        List<Vinyl> vinylsWithLowPhoto = vinylRepository.findByPhotoLowRes_Id(id);
        artists.forEach(artist -> {
            artist.setPhotoId(null);
            artist.setPhoto(null);
        });
        vinylsWithLowPhoto.forEach(vinyl -> {
            vinyl.setPhotoLowRes(null);
        });
        vinylsWithHighPhoto.forEach(vinyl -> {
            vinyl.setPhotoHighRes(null);
            vinyl.setPhotoId(null);
        });
        imageRepository.deleteById(id);
    }

}
