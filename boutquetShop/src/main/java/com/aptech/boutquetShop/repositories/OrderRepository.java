package com.aptech.boutquetShop.repositories;


import com.aptech.boutquetShop.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findByUserId(Integer userId);

    Optional<Orders> findByIdAndUserId(Integer id, Integer userId);
}
