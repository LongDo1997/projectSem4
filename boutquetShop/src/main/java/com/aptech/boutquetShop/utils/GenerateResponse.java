package com.aptech.boutquetShop.utils;

import com.aptech.boutquetShop.utils.data.ResponseData;

import java.util.List;

public class GenerateResponse {

	private static final int SUCCESS_CODE = 20000;
	private static final int ERROR_CODE = 10000;
	public static final int INVALID_TOKEN_ERROR_CODE = 400000;
	public static final int PERMISSION_DENIED_ERROR_CODE = 50001;

	public static <T> ListResponse<T> generateErrorListResponse(String errorMessage) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(ERROR_CODE);
		res.setData(null);
		res.setMessage(errorMessage);
		return res;
	}

	public static <T> ListResponse<T> generateSuccessListResponse(List<T> items, long total) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(SUCCESS_CODE);
		ResponseData<T> data = new ResponseData<T>();
		data.setItems(items);
		data.setTotal(total);
		res.setData(data);
		return res;
	}

	public static <T> BaseResponse generateSuccsesBaseResponse(Object item) {
		BaseResponse res = new BaseResponse();
		res.setCode(SUCCESS_CODE);
		res.setData(item);
		res.setMessage("Thành công");
		return res;
	}


	public static <T> BaseResponse generateErrBaseResponse(String message) {
		BaseResponse res = new BaseResponse();
		res.setCode(SUCCESS_CODE);
		res.setData(null);
		res.setMessage(message);
		return res;
	}
}
