package com.seitov.vinylapi.projection;

public interface ArtistDetails {

    Long getId();
    String getName();
    String getDescription();
    PhotoId getPhoto();

    void setId(Long id);
    void setName(String name);
    void setDescription(String description);
    void setPhoto(PhotoId photoId);

}
