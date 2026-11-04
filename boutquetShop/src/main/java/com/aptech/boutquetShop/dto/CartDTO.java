package com.aptech.boutquetShop.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

public class CartDTO extends SimpleRequestDTO{

    @Data
    @ToString
    public static class CartResponse {
        private String cartId;
        private List<CartItemResponse> cartItemResponses;

    }

    @Data
    @ToString
    public static class CartItemSearchReqDTO extends SimpleRequestDTO {
        private String cartId;
    }

    @Data
    @ToString
    public static class CartItemUpdateReqDTO {
        private Integer bouquetId;

        private Integer quantity;
    }

    @Data
    @ToString
    public static class CartItemResponse {
        private String cartItemId;

        private String bouquetName;

        private String description;

        private String price;

        private String imageUrl;

        private String quantity;

    }

    public interface CartItemSearchResultDTO {
        String getCartItemId();

        String getBouquetName();

        String getDescription();

        String getPrice();

        String getImageUrl();

        String getQuantity();

    }
}
