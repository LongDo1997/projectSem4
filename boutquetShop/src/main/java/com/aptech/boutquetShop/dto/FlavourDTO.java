package com.aptech.boutquetShop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FlavourDTO extends SimpleRequestDTO{
    private String id;
    private String name;
    private String description;
    private String userCreatedId;
    private String userModifyId;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String imageList;
    private List<ImageDTO.ImageSearchResultDTO> imageListDTO;
}
