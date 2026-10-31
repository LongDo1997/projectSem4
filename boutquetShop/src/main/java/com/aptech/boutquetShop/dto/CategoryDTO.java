package com.aptech.boutquetShop.dto;

import lombok.Data;

@Data
public class CategoryDTO extends SimpleRequestDTO{
    private Integer id;
    private String name;
    private Integer type;
    private Integer status;
}
