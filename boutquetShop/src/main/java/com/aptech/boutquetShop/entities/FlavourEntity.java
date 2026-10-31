package com.aptech.boutquetShop.entities;

import lombok.Data;

import javax.persistence.*;

@Table(name = "Flavour")
@Entity
@Data
public class FlavourEntity extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ImageList")
    private String imageList;
}