package com.aptech.boutquetShop.repositories;


import com.aptech.boutquetShop.dto.CartDTO;
import com.aptech.boutquetShop.dto.OrderDTO;
import com.aptech.boutquetShop.entities.CartItems;
import com.aptech.boutquetShop.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderItemsRepository extends JpaRepository<OrderItems,Integer> {
	@Query( value="SELECT OI.Id As OrderItemId" +
			"			  ,B.Name AS BouquetName"
			+ "			  ,CONCAT(B.Description,'') AS Description"
			+ "			  ,OI.UnitPrice AS Price"
			+ "			  ,B.ImageUrl"
			+ "			  ,OI.Quantity"
			+ "			FROM OrderItems OI "
			+ "			JOIN Bouquets B ON OI.BouquetId = B.Id "
			+ "			WHERE 1=1"
			+ "			AND (:#{#dto.orderId} IS NULL OR OI.OrderId = :#{#dto.orderId})"
			+ "         ORDER BY OI.Id "
			,countQuery = "SELECT count(OI.id) "
			+ "			FROM OrderItems OI "
			+ "			JOIN Bouquets B ON OI.BouquetId = B.Id "
			+ "			WHERE 1=1"
			+ "			AND (:#{#dto.orderId} IS NULL OR OI.OrderId = :#{#dto.orderId})"
			, nativeQuery = true)
	List<OrderDTO.OrderItemSearchResultDTO> getOrderItemList(OrderDTO.OrderItemReq dto);

}
