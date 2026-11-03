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

	public static <T> ListResponse<T> generateSuccessListResponse(List<T> items, long total, int totalPage, int size, int currentPage) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(SUCCESS_CODE);
		ResponseData<T> data = new ResponseData<T>();
		data.setItems(items);
		data.setTotal(total);
		data.setSize(size);
		data.setCurrentPage(currentPage);
		data.setNumberOfElements(items.size());
		data.setTotalPages(totalPage);

		res.setData(data);
		return res;
	}

	public static <T> ListResponse<T> generateSuccessListResponse(List<T> items) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(SUCCESS_CODE);
		ResponseData<T> data = new ResponseData<T>();
		data.setItems(items);
		data.setTotal(items.size());
		res.setData(data);
		return res;
	}

	public static <T> BaseResponse<T> generateSuccsesBaseResponse(Object item) {
		BaseResponse res = new BaseResponse();
		res.setCode(SUCCESS_CODE);
		res.setData(item);
		res.setMessage("Success");
		return res;
	}


	public static <T> BaseResponse<T> generateErrBaseResponse(String message) {
		BaseResponse res = new BaseResponse();
		res.setCode(ERROR_CODE);
		res.setData(null);
		res.setMessage(message);
		return res;
	}
}
