package com.seitov.vinylapi.projection;

import com.seitov.vinylapi.entity.Format;

import java.util.List;

public interface VinylLight {

    Long getId();
    String getName();
    Double getPrice();
    List<ArtistName> getArtists();
    Format getFormat();
    PhotoId getPhotoLowRes();

    void setId(Long id);
    void setName(String name);
    void setPrice(Double price);
    void setArtists(List<ArtistName> artists);
    void setFormat(Format name);
    void setPhotoLowRes(PhotoId photoId);

}
