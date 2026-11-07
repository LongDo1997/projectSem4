package com.aptech.boutquetShop.entities;

import lombok.*;

import javax.persistence.*;

@Table(name = "Recipients")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "occasionId")
    private Integer OccasionId;

    @Column(name = "Content")
    private String content;
}