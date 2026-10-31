package com.aptech.boutquetShop.dto;

import java.util.Date;

import com.aptech.boutquetShop.dto.BaseDTO.BaseResponseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class UserAccountDTO extends SimpleRequestDTO {

	@Data
	@ToString
	public static class UserAccountReqDTO extends SimpleRequestDTO {
		private String userName;
		private String password;
	}

	public interface UserAccountResultDTO {
		String getId();

		String getFirstName();

		String getLastName();

		String getName();

		String getEmail();

		String getPasswordHash();

		String getPassword();

		String getDob();

		String getGender();

		String getPhone();

		String getAddress();

		String getCreatedAt();

		String getRole();

	}

	@Data
	@ToString
	public static class UserAccountResponseDTO extends BaseResponseDTO {

		private int id;

		private String name;

		private String description;

		private String email;

		private String phoneNumber;

		private String password;

		private String imageList;

		private Integer type;

		private String roleId;

		private Date valiToDt;

	}

	@Data
	@ToString
	@NoArgsConstructor
	public static class AuthResponseDTO {
		private String accessToken;
		private String tokenType = "Bearer ";

		public AuthResponseDTO(String accessToken) {
			this.accessToken = accessToken;
		}

	}

	@Data
	@ToString
	public static class UserAccountRegDTO {
		private String firstName;

		private String lastName;

		private String email;

		private String password;

		private String dob;

		private Integer gender;

		private String phone;

		private String address;

		private String createdAt;

		private String role;
	}
}
