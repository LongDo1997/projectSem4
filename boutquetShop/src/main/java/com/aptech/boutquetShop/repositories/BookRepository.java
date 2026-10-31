package com.aptech.boutquetShop.repositories;


public interface BookRepository {

//	@Query(value = "select b.* from Book b where (:name is null or b.Name like %:name%)", countQuery = "select b.* from Book b where (:name is null or b.Name like %:name%)", nativeQuery = true)
//	Page<BookSearchResultDTO> getAll(String name, Pageable pageable);
//
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
////			+ "         ORDER BY B.Id DESC "
////    		+ "			AND B.ValiToDt > CURRENT_TIMESTAMP"
//    		,countQuery = "SELECT count(B.id) "
//    		+ "			FROM BOOK B "
//    		+ "			WHERE 1=1"
//    		+ "			AND (:#{#dto.id} IS NULL OR b.Id LIKE %:#{#dto.id}%)"
//    		+ "			AND (:#{#dto.name} IS NULL OR b.Name LIKE %:#{#dto.name}%)"
//    		+ "			AND (:#{#dto.author} IS NULL OR b.Author LIKE %:#{#dto.author}%)"
//    		+ "			AND (:#{#dto.priceFr} = 0 OR (b.Price BETWEEN :#{#dto.priceFr} AND :#{#dto.priceTo}))"
////			+ "         ORDER BY B.Id DESC "
////    		+ "			AND B.ValiToDt > CURRENT_TIMESTAMP"
//    		, nativeQuery = true)
//	Page<BookSearchResultDTO> findBookDTOTest(BookSearchReqDTO dto, Pageable pageable);
//
//	@Query(value = "SELECT b.Id"
//			+ "			  ,b.Name"
//			+ "			  ,b.Author"
//			+ "			  ,CONCAT(b.Description,'') AS Description"
//			+ "			  ,b.Price"
//			+ "			  ,b.Quantity"
//			+ "			  ,b.ImageList"
//			+ "			  ,b.UserCreatedId"
////    		+ "			  ,(SELECT Name FROM UserAccount WHERE Id = b.UserCreatedId) AS UserCreated"
//			+ "			  ,b.UserModifyId"
////    		+ "			  ,(SELECT Name FROM UserAccount WHERE Id = b.UserModifyId) AS UserModified"
//			+ "			  ,b.Status"
//			+ "			  ,b.CreatedAt"
//			+ "			  ,b.UpdatedAt"
//			+ "			  ,b.ValiToDt"
//			+ "			  ,c.Name as categoryName"
//			+ "			FROM Book b "
//			+ "			LEFT JOIN Category c ON c.Id = b.CategoryId "
//			+ "			WHERE 1=1"
//			+ "			AND (:#{#dto.id} IS NULL OR b.Id LIKE %:#{#dto.id}%)"
//			+ "			AND (:#{#dto.name} IS NULL OR b.Name LIKE %:#{#dto.name}%)"
//			+ "			AND (:#{#dto.author} IS NULL OR b.Author LIKE %:#{#dto.author}%)"
//			+ "			AND (:#{#dto.priceFr} = 0 OR (b.Price BETWEEN :#{#dto.priceFr} AND :#{#dto.priceTo})) " ,nativeQuery = true)
//	Page<BookSearchResultDTO> findBookDTOTest2(BookSearchReqDTO dto, Pageable pageable);
//
//	BookEntity findByNameAndAuthor(String name, String author);
//
//	BookEntity findByIdAndValiToDtGreaterThanEqual(Integer id, String current);
//
//	@Query(value = "UPDATE Book SET Status = 0 WHERE Id = :id ",nativeQuery = true)
//	@Modifying
//	void updateStatus(Integer id);
}
