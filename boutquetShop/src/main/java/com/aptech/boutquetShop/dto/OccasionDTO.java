package com.aptech.boutquetShop.dto;

import lombok.Data;
import lombok.ToString;

public class OccasionDTO extends SimpleRequestDTO{
    @Data
    @ToString
    public static class OccasionSearchReqDTO extends SimpleRequestDTO {
        private String id;
        private String name;

    }

    @Data
    @ToString
    public static class OccasionSearchResponseDTO {
        private String id;

        private String name;
    }
}
