package com.aptech.boutquetShop.entities;

import lombok.Data;

import javax.persistence.*;

@Table(name = "Recipe")
@Entity
@Data
public class RecipeEntity extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "AccessType")
    private Integer accessType;

    @Basic
    @Column(name = "ImageList")
    private String imageList;
}