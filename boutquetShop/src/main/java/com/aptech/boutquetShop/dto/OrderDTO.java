package com.aptech.boutquetShop.dto;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class OrderDTO extends SimpleRequestDTO{

    @Data
    @ToString
    public static class OrderListDTO {
        private Integer id;

        private Integer userId;

        private Double totalAmount;

        private String status;

        private String statusName;

        private String paymentMethod;

        private String paymentStatus;

        private String paymentStatusName;

        private Date deliveryDate;

        private Date createdAt;

    }

    @Data
    @ToString
    public static class OrderDetailResponse {
        private OrderDetailInforResponse orderDetailInfor;
        private OrderRecipientInforResponse orderRecipientInfor;
        private List<OrderItemResponse> orderItems;

    }

    @Data
    @ToString
    public static class OrderDetailInforResponse {
        private Integer id;

        private Double totalAmount;

        private String status;

        private String statusName;

        private String paymentMethod;

        private String paymentStatus;

        private String paymentStatusName;

        private Date deliveryDate;

        private Date createdAt;

    }

    @Data
    @ToString
    public static class OrderItemReq {
        private String orderId;
    }

    public interface OrderItemSearchResultDTO {
        String getOrderItemId();

        String getBouquetName();

        String getDescription();

        String getPrice();

        String getImageUrl();

        String getQuantity();
    }

    @Data
    @ToString
    public static class OrderItemResponse {
        private String bouquetName;

        private String description;

        private  String price;

        private String imageUrl;

        private String quantity;

    }


    @Data
    @ToString
    public static class OrderRecipientInforResponse {
        private String name;

        private String address;

        private String phone;

        private String message;

    }

}
