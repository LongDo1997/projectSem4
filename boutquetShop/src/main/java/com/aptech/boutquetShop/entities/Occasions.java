package com.aptech.boutquetShop.entities;

import javax.persistence.*;

import lombok.*;

@Table(name = "Occasions")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Occasions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

}