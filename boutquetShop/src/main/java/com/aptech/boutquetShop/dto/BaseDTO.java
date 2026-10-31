package com.aptech.boutquetShop.dto;

import java.util.List;

import com.aptech.boutquetShop.dto.ImageDTO.ImageSearchResultDTO;

import lombok.Data;
import lombok.ToString;

public class BaseDTO {
	public interface BaseSearchResultDTO {
		String getUserCreatedId();

		String getUserModifyId();

		String getStatus();

		String getCreatedAt();

		String getUpdatedAt();
	}

	@Data
	@ToString
	public static class BaseResponseDTO {
		private String userCreatedId;

		private String userModifyId;

		private String status;

		private String createdAt;

		private String updatedAt;

		private String imageList;

		private List<ImageSearchResultDTO> imageListDTO;
	}

}
