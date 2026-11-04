package com.aptech.boutquetShop.utils;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResponse<T> {
	Integer code;
	String message;
	String trace;
	String error;
	T data;
	String clientMessageId;
	String transactionId;
	String path;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
	Date timestamp;
	Integer status;

}