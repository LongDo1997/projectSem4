package com.aptech.boutquetShop.entities;

import com.aptech.boutquetShop.utils.Constants;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name = "Orders")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "TotalAmount")
    private Double totalAmount;

    @Column(name = "Status")
    private String status;

    @Column(name = "PaymentMethod")
    private String paymentMethod;

    @Column(name = "PaymentStatus")
    private String paymentStatus;

    @Column(name = "DeliveryDate")
    private Date deliveryDate;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @PrePersist
    private void beforeInsert() {
        this.createdAt = Constants.getCurrentDate();
    }

    @PreUpdate
    private void beforeUpdate() {

    }

}