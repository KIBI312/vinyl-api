package com.seitov.vinylapi.projection;

import com.seitov.vinylapi.entity.Format;
import com.seitov.vinylapi.entity.Genre;

import java.util.List;

public interface VinylDetails {

    Long getId();
    String getName();
    String getDescription();
    Double getPrice();
    List<ArtistName> getArtists();
    List<Genre> getGenres();
    Format getFormat();
    Boolean getInStock();
    String getRecordLabel();
    List<SoundtrackName> getTrackList();
    PhotoId getPhotoHighRes();

    void setId(Long id);
    void setName(String name);
    void setDescription(String description);
    void setPrice(Double price);
    void setArtists(List<ArtistName> artists);
    void setGenres(List<Genre> genres);
    void setFormat(Format format);
    void setInStock(Boolean inStock);
    void setRecordLabel(String recordLabel);
    void setTrackList(List<SoundtrackName> trackList);
    void setPhotoHighRes(PhotoId photoId);

}
