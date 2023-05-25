package com.seitov.vinylapi.entity;


import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Getter
@Table(name = "artist")
public class ArtistShort {

    @Id
    private Long id;
    private String name;

}
