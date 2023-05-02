package com.seitov.vinylapi.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class Artist {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Length(max = 1000)
    private String description;
    @OneToOne
    private Image photo;

}
