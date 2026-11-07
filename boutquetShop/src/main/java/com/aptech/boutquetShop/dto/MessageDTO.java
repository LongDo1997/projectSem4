package com.aptech.boutquetShop.dto;

import lombok.Data;
import lombok.ToString;

public class MessageDTO extends SimpleRequestDTO{

    @Data
    @ToString
    public static class MessageSearchResponseDTO {
        private String occasionId;

        private String content;
    }
}
