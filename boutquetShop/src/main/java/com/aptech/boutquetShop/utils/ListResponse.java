package com.aptech.boutquetShop.utils;

import com.aptech.boutquetShop.utils.data.ResponseData;

public class ListResponse<T> {

	private int code;
	private String message;
	private ResponseData<T> data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ResponseData<T> getData() {
		return data;
	}

	public void setData(ResponseData<T> data) {
		this.data = data;
	}
}
