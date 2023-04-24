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
    @ManyToMany
    @JoinTable(name = "vinyl_category",
            joinColumns = @JoinColumn(name = "vinyl_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
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
