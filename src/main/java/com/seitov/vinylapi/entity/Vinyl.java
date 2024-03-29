package com.seitov.vinylapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vinyl")
public class Vinyl {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Length(max = 1000)
    private String description;
    private Double price;
    private Boolean inStock;
    private String recordLabel;
    @OneToOne
    private Format format;
    @ManyToMany
    private List<Genre> genres;
    @ManyToMany
    private List<ArtistShort> artists;
    @ManyToMany
    @JoinTable(name = "vinyl_soundtrack",
            joinColumns = @JoinColumn(name = "vinyl_id"),
            inverseJoinColumns = @JoinColumn(name = "soundtrack_id"))
    private List<Soundtrack> trackList;
    @ManyToOne(fetch = FetchType.LAZY)
    private Image photoLowRes;
    @ManyToOne(fetch = FetchType.LAZY)
    private Image photoHighRes;
    @Column(name = "photo_high_res_id", updatable = false, insertable = false)
    private Long photoId;

}
