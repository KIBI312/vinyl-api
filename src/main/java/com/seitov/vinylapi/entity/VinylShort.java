package com.seitov.vinylapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.List;

@Entity
@Immutable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vinyl")
public class VinylShort {

    @Id
    private Long id;
    private String name;
    private Double price;
    @ManyToMany
    @JoinTable(name = "vinyl_artists",
            joinColumns = { @JoinColumn(name = "vinyl_id")},
            inverseJoinColumns = {@JoinColumn(name = "artists_id")})
    private List<ArtistShort> artists;
    @OneToOne
    private Format format;
    @Column(name = "photo_low_res_id")
    private Long photoId;

}
