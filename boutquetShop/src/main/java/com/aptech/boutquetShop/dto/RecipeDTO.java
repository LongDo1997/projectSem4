package com.aptech.boutquetShop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RecipeDTO extends SimpleRequestDTO {
    private String id;
    private String name;
    private String description;
    private Integer accessType;
    private String userCreatedId;
    private String userModifyId;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String imageList;
    private String flavourName;
    private List<ImageDTO.ImageSearchResultDTO> imageListDTO;
    private List<FlavourDTO> flavourDTOs;
    private Integer rowNum;

    public interface RecipeView {
          String getName();
          String getDescription();
          Integer getAccessType();
          Integer getStatus();
          String getFlavourName();
          String getRowNum();
    }

}
