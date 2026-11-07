package com.aptech.boutquetShop.repositories;


import com.aptech.boutquetShop.entities.Occasions;
import com.aptech.boutquetShop.entities.Recipients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipientsRepository extends JpaRepository<Recipients, Integer> {
    Optional<Recipients> findByOrderId(Integer orderId);
}
