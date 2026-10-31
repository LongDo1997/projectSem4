package com.aptech.boutquetShop.repositories;

import com.aptech.boutquetShop.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aptech.boutquetShop.dto.UserAccountDTO.UserAccountReqDTO;
import com.aptech.boutquetShop.dto.UserAccountDTO.UserAccountResultDTO;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

	@Query(value = "Select UA.id"
			+ "	  		  ,UA.firstName"
			+ "	  		  ,UA.lastName"
			+ "	  		  ,CONCAT(UA.firstName,' ',UA.lastName) AS name"
			+ "	  		  ,UA.email"
			+ "	  		  ,UA.passwordHash"
			+ "	  		  ,UA.passwordHash AS password"
			+ "	  		  ,UA.dob"
			+ "	  		  ,UA.gender"
			+ "	  		  ,UA.phone"
			+ "	  		  ,UA.address"
			+ "	  		  ,UA.createdAt"
			+ "	 		  ,UA.role"
//			+ "			  ,("
//			+ "		            SELECT R.Name + ', ' AS [text()]"
//			+ "						FROM Role R"
//			+ "						WHERE R.Id In (SELECT value FROM STRING_SPLIT(CONCAT(UA.RoleId,''),','))"
//			+ "						AND R.ValiToDt > CURRENT_TIMESTAMP"
//			+ "						FOR XML PATH (''), TYPE"
//			+ "		        ).value('text()[1]','nvarchar(max)') AS RoleList"
			+ "		FROM Users UA "
			+ "		WHERE 1=1"
			+ "		 AND (:#{#dto.userName} IS NULL OR UA.email LIKE %:#{#dto.userName}%)"
//			+ "		 AND UA.ValiToDt > CURRENT_TIMESTAMP"
			, nativeQuery = true
			)
	UserAccountResultDTO getUserInfor(UserAccountReqDTO dto);

	Optional<Users> findByEmail(String email);

}
