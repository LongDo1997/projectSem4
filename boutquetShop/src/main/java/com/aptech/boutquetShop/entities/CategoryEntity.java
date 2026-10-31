package com.aptech.boutquetShop.entities;

import javax.persistence.*;

import lombok.Data;

@Table(name = "Category")
@Entity
@Data
public class CategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private Integer type;
}