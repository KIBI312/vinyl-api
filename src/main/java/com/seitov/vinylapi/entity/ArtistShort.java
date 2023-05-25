package com.seitov.vinylapi.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "artist")
public class ArtistShort {

    @Id
    private Long id;
    private String name;

}
