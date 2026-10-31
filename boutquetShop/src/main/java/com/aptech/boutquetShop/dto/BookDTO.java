package com.aptech.boutquetShop.dto;

import com.aptech.boutquetShop.dto.BaseDTO.BaseResponseDTO;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

public class BookDTO extends SimpleRequestDTO {

	@Data
	@ToString
	public static class BookSearchReqDTO extends SimpleRequestDTO {
		private String id;
		private String name;
		private String author;
		private float priceFr;
		private float priceTo;
		private MultipartFile[] file;
	}

	@Data
	@ToString
	public static class BookInsertReqDTO {
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

	public interface BookSearchResultDTO {
		String getId();

		String getName();

		String getAuthor();

		String getDescription();

		String getPrice();

		String getQuantity();

		String getImageList();

		String getUserCreated();

		String getUserModified();

		String getValiToDt();

		String getUserCreatedId();

		String getUserModifyId();

		String getStatus();

		String getCreatedAt();

		String getUpdatedAt();

		String getCategoryName();

	}

	@Data
	@ToString
	public static class BookSearchResponseDTO extends BaseResponseDTO {

		private String id;

		private String name;

		private String author;

		private String description;

		private String price;

		private String quantity;

		private String userCreated;
		
		private String categoryName;

	}
}
