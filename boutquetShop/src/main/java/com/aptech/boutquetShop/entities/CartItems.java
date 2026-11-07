package com.aptech.boutquetShop.entities;

import com.aptech.boutquetShop.utils.Constants;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name = "CartItems")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CartId")
    private Integer cartId;

    @Column(name = "BouquetId")
    private Integer bouquetId;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "UnitPrice")
    private Double unitPrice;


}