package com.seitov.vinylapi.projection;

import java.util.List;

public interface VinylLight {

    Long getId();
    String getName();
    Double getPrice();
    List<ArtistName> getArtists();
    FormatName getFormat();
    PhotoId getPhotoLowRes();

    interface FormatName {
        String getName();
    }

    interface ArtistName {
        String getName();
    }

    interface PhotoId {
        Long getId();
    }

}
