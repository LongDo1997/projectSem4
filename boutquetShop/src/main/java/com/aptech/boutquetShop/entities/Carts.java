package com.aptech.boutquetShop.entities;

import com.aptech.boutquetShop.utils.Constants;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name = "Carts")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Carts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "UserId")
    private Integer userId;

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