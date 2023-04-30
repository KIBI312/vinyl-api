package com.seitov.vinylapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Vinyl {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean inStock;
    private String recordLabel;
    @OneToOne
    private Format format;
    @OneToMany
    private List<Genre> genres;
    @OneToMany
    private List<Artist> artists;
    @ManyToMany
    @JoinTable(name = "vinyl_soundtrack",
            joinColumns = @JoinColumn(name = "vinyl_id"),
            inverseJoinColumns = @JoinColumn(name = "soundtrack_id"))
    private List<Soundtrack> trackList;
    @OneToOne
    private Image photoLowRes;
    @OneToOne
    private Image photoHighRes;

}
