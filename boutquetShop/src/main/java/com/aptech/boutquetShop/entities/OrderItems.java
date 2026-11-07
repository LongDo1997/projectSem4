package com.aptech.boutquetShop.entities;

import lombok.*;

import javax.persistence.*;

@Table(name = "OrderItems")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "OrderId")
    private Integer orderId;

    @Column(name = "BouquetId")
    private Integer bouquetId;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "UnitPrice")
    private Double unitPrice;


}