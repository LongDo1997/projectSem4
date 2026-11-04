package com.aptech.boutquetShop.repositories;


import com.aptech.boutquetShop.entities.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Carts,Integer> {
    Optional<Carts> findByUserId(Integer userId);
}
