package com.aptech.boutquetShop.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    public static ResponseEntity<Object> getResponseEntityStatus(Object itemObject) {
        BaseResponse baseResponse = new BaseResponse();
        if (itemObject != null) {
            baseResponse.setData(itemObject);
        }
        baseResponse.setCode(200);
//        baseResponse.setMessage(ErrorApp.SUCCESS.getDescription());
        baseResponse.setStatus(20000);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

//    public static ResponseEntity<Object> getResponseEntityStatus(ErrorApp errorApp, HttpStatus httpStatus) {
//        BaseResponse baseResponse = new BaseResponse();
//        baseResponse.setCode(errorApp.getCode());
//        baseResponse.setMessage(errorApp.getDescription());
//        baseResponse.setStatus(STATUS_FAIL);
//        if (httpStatus == null) httpStatus = HttpStatus.OK;
//        return new ResponseEntity<>(baseResponse, httpStatus);
//    }

}