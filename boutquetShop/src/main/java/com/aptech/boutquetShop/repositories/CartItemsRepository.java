package com.aptech.boutquetShop.repositories;


import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.dto.CartDTO;
import com.aptech.boutquetShop.entities.Bouquets;
import com.aptech.boutquetShop.entities.CartItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems,Integer> {
	@Query(value = "SELECT CI.Id AS CartItemId, B.Id AS BouquetId, B.Name AS BouquetName, CONCAT(B.Description,'') AS Description, B.Price, B.ImageUrl, CI.Quantity FROM CartItems CI JOIN Bouquets B ON CI.BouquetId = B.Id WHERE 1=1 AND (:#{#dto.cartId} IS NULL OR CI.CartId = :#{#dto.cartId}) AND B.IsActive = 'true' ORDER BY CI.Id", countQuery = "SELECT count(CI.id) FROM CartItems CI JOIN Bouquets B ON CI.BouquetId = B.Id WHERE 1=1 AND (:#{#dto.cartId} IS NULL OR CI.CartId = :#{#dto.cartId}) AND B.IsActive = 'true'", nativeQuery = true)
	List<CartDTO.CartItemSearchResultDTO> getCartItemList(CartDTO.CartItemSearchReqDTO dto);

	Optional<CartItems> findByCartIdAndBouquetId(Integer cartId, Integer bouquetId);
}
