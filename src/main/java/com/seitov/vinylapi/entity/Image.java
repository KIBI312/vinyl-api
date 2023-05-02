package com.seitov.vinylapi.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue
    private Long id;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;

}

