package com.aptech.boutquetShop.repositories;


import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.entities.Bouquets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BouquetRepository extends JpaRepository<Bouquets,Integer> {
	@Query( value="SELECT B.Id"
    		+ "			  ,B.Name"
    		+ "			  ,CONCAT(B.Description,'') AS Description"
    		+ "			  ,B.Price"
    		+ "			  ,B.ImageUrl"
    		+ "			  ,B.IsActive"
    		+ "			  ,B.CreatedAt"
    		+ "			  ,C.Name as OccasionName"
    		+ "			FROM Bouquets B "
    		+ "			LEFT JOIN Occasions C ON C.Id = B.OccasionId "
    		+ "			WHERE 1=1"
    		+ "			AND (:#{#dto.id} IS NULL OR b.Id = :#{#dto.id})"
    		+ "			AND (:#{#dto.name} IS NULL OR b.Name LIKE %:#{#dto.name}% OR b.Description LIKE %:#{#dto.name}%)"
    		+ "			AND (:#{#dto.priceFr} = 0 OR (b.Price BETWEEN :#{#dto.priceFr} AND :#{#dto.priceTo}))"
			+ "			AND (:#{#dto.isActive} IS NULL OR B.IsActive = :#{#dto.isActive})"
			+ "			AND (:#{#dto.occasionId} IS NULL OR b.OccasionId = :#{#dto.occasionId})"
            + "         ORDER BY B.Id "
    		,countQuery = "SELECT count(B.id) "
    		+ "			FROM Bouquets B "
    		+ "			WHERE 1=1"
            + "			AND (:#{#dto.id} IS NULL OR b.Id = :#{#dto.id})"
            + "			AND (:#{#dto.name} IS NULL OR b.Name LIKE %:#{#dto.name}% OR b.Description LIKE %:#{#dto.name}%)"
            + "			AND (:#{#dto.priceFr} = 0 OR (b.Price BETWEEN :#{#dto.priceFr} AND :#{#dto.priceTo}))"
			+ "			AND (:#{#dto.isActive} IS NULL OR B.IsActive = :#{#dto.isActive})"
            + "			AND (:#{#dto.occasionId} IS NULL OR b.OccasionId = :#{#dto.occasionId})"
    		, nativeQuery = true)
    Page<BouquetDTO.BouquetSearchResultDTO> getBouquetListPaing(BouquetDTO.BouquetSearchReqDTO dto, Pageable pageable);

	@Query( value="SELECT B.Id"
			+ "			  ,B.Name"
			+ "			  ,CONCAT(B.Description,'') AS Description"
			+ "			  ,B.Price"
			+ "			  ,B.ImageUrl"
			+ "			  ,B.IsActive"
			+ "			  ,B.CreatedAt"
			+ "			  ,C.Name as OccasionName"
			+ "			FROM Bouquets B "
			+ "			LEFT JOIN Occasions C ON C.Id = B.OccasionId "
			+ "			WHERE 1=1"
			+ "			AND (:#{#dto.id} IS NULL OR b.Id = :#{#dto.id})"
			+ "			AND (:#{#dto.name} IS NULL OR b.Name LIKE %:#{#dto.name}% OR b.Description LIKE %:#{#dto.name}%)"
			+ "			AND (:#{#dto.priceFr} = 0 OR (b.Price BETWEEN :#{#dto.priceFr} AND :#{#dto.priceTo}))"
			+ "			AND (:#{#dto.isActive} IS NULL OR B.IsActive = :#{#dto.isActive})"
			+ "			AND (:#{#dto.occasionId} IS NULL OR b.OccasionId = :#{#dto.occasionId})"
			+ "         ORDER BY B.Id "
			,countQuery = "SELECT count(B.id) "
			+ "			FROM Bouquets B "
			+ "			WHERE 1=1"
			+ "			AND (:#{#dto.id} IS NULL OR b.Id = :#{#dto.id})"
			+ "			AND (:#{#dto.name} IS NULL OR b.Name LIKE %:#{#dto.name}% OR b.Description LIKE %:#{#dto.name}%)"
			+ "			AND (:#{#dto.priceFr} = 0 OR (b.Price BETWEEN :#{#dto.priceFr} AND :#{#dto.priceTo}))"
			+ "			AND (:#{#dto.isActive} IS NULL OR B.IsActive = :#{#dto.isActive})"
			+ "			AND (:#{#dto.occasionId} IS NULL OR b.OccasionId = :#{#dto.occasionId})"
			, nativeQuery = true)
	BouquetDTO.BouquetSearchResultDTO getBouquetDetail(BouquetDTO.BouquetSearchReqDTO dto);
}
