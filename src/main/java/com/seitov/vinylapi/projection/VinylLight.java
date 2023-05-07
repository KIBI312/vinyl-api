package com.seitov.vinylapi.projection;

import java.util.List;

public interface VinylLight {

    Long getId();
    String getName();
    Double getPrice();
    List<ArtistName> getArtists();
    FormatName getFormat();
    PhotoId getPhotoLowRes();

    void setId(Long id);
    void setName(String name);
    void setPrice(Double price);
    void setArtists(List<ArtistName> artists);
    void setFormat(FormatName name);
    void setPhotoLowRes(PhotoId photoId);

}
