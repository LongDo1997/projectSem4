package com.aptech.boutquetShop.repositories;

import java.util.List;

public interface ImageRepository {

//	@Query( value="Select I.Id"
//    		+ "			  ,I.FileName"
//    		+ "			  ,I.FilePath"
//    		+ "			  ,I.OrgFileName"
//    		+ "			  ,I.orderNum"
//    		+ "			  ,I.CreatedAt"
//    		+ "			  ,I.UpdatedAt"
//    		+ "			  ,I.UserCreatedId"
//    		+ "			  ,(SELECT Name FROM UserAccount WHERE Id = I.UserCreatedId) AS UserCreated"
//    		+ "			  ,I.UserModifyId"
//    		+ "			  ,(SELECT Name FROM UserAccount WHERE Id = I.UserModifyId) AS UserModified"
//    		+ "		 FROM Image I"
//    		+ "		 WHERE 1=1"
//    		+ "		 AND I.Id IN (:#{#dto.ids})"
//    		+ "		 Order by I.orderNum", nativeQuery = true)
//    List<ImageSearchResultDTO> findImageDTOList(ImageSearchReqDTO dto);
//
//    @Query( value="Select I.Id"
//    		+ "			  ,I.FileName"
//    		+ "			  ,I.FilePath"
//    		+ "			  ,I.OrgFileName"
//    		+ "			  ,I.orderNum"
//    		+ "			  ,I.CreatedAt"
//    		+ "			  ,I.UpdatedAt"
//    		+ "			  ,I.UserCreatedId"
//    		+ "			  ,(SELECT Name FROM UserAccount WHERE Id = I.UserCreatedId) AS UserCreated"
//    		+ "			  ,I.UserModifyId"
//    		+ "			  ,(SELECT Name FROM UserAccount WHERE Id = I.UserModifyId) AS UserModified"
//    		+ "		 FROM Image I"
//    		+ "		 WHERE 1=1"
//    		+ "		 AND CONCAT(I.FilePath,'') = :#{#dto.filePath}"
//    		+ "		 AND I.FileName IN (:#{#dto.fileNames})"
//    		+ "		 Order by I.orderNum", nativeQuery = true)
//    List<ImageSearchResultDTO> findImageDTOListByFileName(ImageUploadedDTO dto);
}
