package com.aptech.boutquetShop.dto;

import java.sql.Clob;
import java.sql.NClob;
import java.util.Date;
import java.util.List;

import javax.persistence.Lob;

import com.aptech.boutquetShop.dto.BaseDTO.BaseSearchResultDTO;
import com.aptech.boutquetShop.entities.ImageEntity;

import lombok.Data;
import lombok.ToString;

public class ImageDTO {

	@Data
	@ToString
	public static class ImageSearchReqDTO extends SimpleRequestDTO {
		private String id;
		private List<String> ids;
		private List<String> fileNames;
		private String filePath;
	}

	@Data
	@ToString
	public static class ImageUploadedDTO {
		private List<ImageEntity> imageEntities;
		private List<String> fileNames;

		@Lob
		private String filePath;
	}

	public interface ImageSearchResultDTO extends BaseSearchResultDTO {
		String getId();

		String getFileName();

		String getFilePath();

		String getOrgFileName();

		Integer getOrderNum();
	}
}
