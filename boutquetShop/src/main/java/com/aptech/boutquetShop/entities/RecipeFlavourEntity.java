package com.aptech.boutquetShop.entities;

import lombok.Data;

import javax.persistence.*;

@Table(name = "RecipeFlavour")
@Entity
@Data
public class RecipeFlavourEntity extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "Id")
    private Integer id;

    @Column(name = "RecipeId")
    private Integer recipeId;

    @Column(name = "FlavourId")
    private Integer flavourId;

    @Column(name = "Unit")
    private Integer unit;

    @Column(name = "Description")
    private String description;
}