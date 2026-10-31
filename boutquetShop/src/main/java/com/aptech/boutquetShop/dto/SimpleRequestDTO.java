package com.aptech.boutquetShop.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class SimpleRequestDTO {
    private Integer size = 20;
    private Integer page = 1;
}
