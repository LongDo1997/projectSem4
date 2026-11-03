package com.aptech.boutquetShop.dto;

import com.aptech.boutquetShop.dto.BaseDTO.BaseResponseDTO;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

public class BouquetDTO extends SimpleRequestDTO {

	@Data
	@ToString
	public static class BouquetSearchReqDTO extends SimpleRequestDTO {
		private String id;
		private String name;
		private float priceFr;
		private float priceTo;
		private Integer occasionId;
		private boolean isActive;

	}

	@Data
	@ToString
	public static class BouquetInsertReqDTO {
		private Integer id;

		private String name;

		private String author;

		private String description;

		private String price;

		private String quantity;

		private String userCreatedId;

		private String userModifyId;

		private String status;

		private String imageList;

		private Integer categoryId;

		private MultipartFile[] file;
	}

	public interface BouquetSearchResultDTO {
		String getId();

		String getName();

		String getDescription();

		String getPrice();

		String getImageUrl();

		String getIsActive();

		String getCreatedAt();

		String getOccasionName();

	}

	@Data
	@ToString
	public static class BouquetSearchResponseDTO {

		private String id;

		private String name;

		private String description;

		private String price;

		private String imageUrl;
		
		private String occasionName;

	}
}
