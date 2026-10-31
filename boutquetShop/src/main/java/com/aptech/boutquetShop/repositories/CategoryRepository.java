package com.aptech.boutquetShop.repositories;


public interface CategoryRepository {
//	@Query(value = "select c.* from Category c where (:name is null or c.Name like %:name%)",
//			countQuery = "select count(*) from Category c where (:name is null or c.Name like %:name%)", nativeQuery = true)
//	Page<CategoryEntity> getAll(String name, Pageable pageable);
//
//	CategoryEntity findAllById(Integer id);

//	@Query( value="SELECT B.Id"
//    		+ "			  ,B.Name"
//    		+ "			  ,B.Author"
//    		+ "			  ,CONCAT(B.Description,'') AS Description"
//    		+ "			  ,B.Price"
//    		+ "			  ,B.Quantity"
//    		+ "			  ,B.ImageList"
//    		+ "			  ,B.UserCreatedId"
////    		+ "			  ,(SELECT Name FROM UserAccount WHERE Id = B.UserCreatedId) AS UserCreated"
//    		+ "			  ,B.UserModifyId"
////    		+ "			  ,(SELECT Name FROM UserAccount WHERE Id = B.UserModifyId) AS UserModified"
//    		+ "			  ,B.Status"
//    		+ "			  ,B.CreatedAt"
//    		+ "			  ,B.UpdatedAt"
//    		+ "			  ,B.ValiToDt"
//    		+ "			  ,c.Name as categoryName"
//    		+ "			FROM BOOK B "
//    		+ "			LEFT JOIN Category c ON c.Id = B.CategoryId "
//    		+ "			WHERE 1=1"
//    		+ "			AND (:#{#dto.id} IS NULL OR b.Id LIKE %:#{#dto.id}%)"
//    		+ "			AND (:#{#dto.name} IS NULL OR b.Name LIKE %:#{#dto.name}%)"
//    		+ "			AND (:#{#dto.author} IS NULL OR b.Author LIKE %:#{#dto.author}%)"
//    		+ "			AND (:#{#dto.priceFr} = 0 OR (b.Price BETWEEN :#{#dto.priceFr} AND :#{#dto.priceTo}))"
////    		+ "			AND B.ValiToDt > CURRENT_TIMESTAMP"
//    		,countQuery = "SELECT B.id"
//    		+ "			FROM BOOK B "
//    		+ "			WHERE 1=1"
//    		+ "			AND (:#{#dto.id} IS NULL OR b.Id LIKE %:#{#dto.id}%)"
//    		+ "			AND (:#{#dto.name} IS NULL OR b.Name LIKE %:#{#dto.name}%)"
//    		+ "			AND (:#{#dto.author} IS NULL OR b.Author LIKE %:#{#dto.author}%)"
//    		+ "			AND (:#{#dto.priceFr} = 0 OR (b.Price BETWEEN :#{#dto.priceFr} AND :#{#dto.priceTo}))"
////    		+ "			AND B.ValiToDt > CURRENT_TIMESTAMP"
//    		, nativeQuery = true)
//	Page<BookSearchResultDTO> findBookDTOTest(BookSearchReqDTO dto, Pageable pageable);
//
//	BookEntity findByNameAndAuthor(String name, String author);
//
//	BookEntity findByIdAndValiToDtGreaterThanEqual(Integer id, String current);
//
//	@Query(value = "UPDATE Book SET Status = 0 WHERE Id = :id ",nativeQuery = true)
//	@Modifying
//	void updateStatus(Integer id);
}
