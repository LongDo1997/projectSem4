package com.aptech.boutquetShop.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

public class OrderProcessDTO {
    @Data
    @ToString
    public static class OrderBookingReqDTO {
        private Integer orderId;

        private String paymentMethod;

        private RecipienttReqDTO receipentReq;
    }

    @Data
    @ToString
    public static class RecipienttReqDTO {
        private String name;

        private String address;

        private String phone;

        private String message;

    }

}
