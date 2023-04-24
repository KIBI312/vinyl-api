package com.seitov.vinylapi.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @OneToOne
    private Image photo;
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        artist, genre, format, decade
    }

}
