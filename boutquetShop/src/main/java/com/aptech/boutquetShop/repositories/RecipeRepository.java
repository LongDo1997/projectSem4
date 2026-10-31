package com.aptech.boutquetShop.repositories;

public interface RecipeRepository {

//    @Query(value = "SELECT * FROM (" +
//            "SELECT r.Name, " +
//            "       CAST(r.Description AS nvarchar(MAX)) AS Description, " +
//            "       r.AccessType, " +
//            "       STRING_AGG(f.Name, ',') AS flavourName, " +
//            "       ROW_NUMBER() OVER (ORDER BY r.Name) AS row_num " +
//            "FROM Recipe r " +
//            "LEFT JOIN RecipeFlavour rf ON rf.RecipeId = r.Id " +
//            "LEFT JOIN Flavour f ON f.Id = rf.FlavourId " +
//            "GROUP BY r.Name, CAST(r.Description AS nvarchar(MAX)), r.AccessType" +
//            ") AS grouped_result " +
//            "WHERE row_num BETWEEN (:#{#dto.page} * :#{#dto.size} - (:#{#dto.size} - 1)) AND (:#{#dto.page} * :#{#dto.size}) ",
//            countQuery = "SELECT COUNT(*) FROM (" +
//                    "SELECT r.Name, " +
//                    "       CAST(r.Description AS nvarchar(MAX)) AS Description, " +
//                    "       r.AccessType, " +
//                    "       STRING_AGG(f.Name, ',') AS flavourName, " +
//                    "       ROW_NUMBER() OVER (ORDER BY r.Name) AS row_num " +
//                    "FROM Recipe r " +
//                    "LEFT JOIN RecipeFlavour rf ON rf.RecipeId = r.Id " +
//                    "LEFT JOIN Flavour f ON f.Id = rf.FlavourId " +
//                    "GROUP BY r.Name, CAST(r.Description AS nvarchar(MAX)), r.AccessType" +
//                    ") AS count_result " +
//                    "WHERE row_num BETWEEN (:#{#dto.page} * :#{#dto.size} - (:#{#dto.size} - 1)) AND (:#{#dto.page} * :#{#dto.size}) ",
//            nativeQuery = true)
//    Page<RecipeDTO.RecipeView> getAll(RecipeDTO dto, Pageable pageable);


//
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
