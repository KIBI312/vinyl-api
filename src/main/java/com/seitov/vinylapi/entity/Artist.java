package com.seitov.vinylapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Length(max = 1000)
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    private Image photo;
    @Column(name = "photo_id", updatable = false, insertable = false)
    private Long photoId;

}
