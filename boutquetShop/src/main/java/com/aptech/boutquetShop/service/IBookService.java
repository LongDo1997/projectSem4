package com.aptech.boutquetShop.service;

import java.util.List;

import com.aptech.boutquetShop.dto.BookDTO;
import com.aptech.boutquetShop.dto.BookDTO.BookSearchReqDTO;
import com.aptech.boutquetShop.dto.BookDTO.BookSearchResultDTO;
import com.aptech.boutquetShop.entities.BookEntity;
import com.aptech.boutquetShop.utils.ListResponse;

public interface IBookService {
	ListResponse<BookEntity> getAll(BookDTO req);

	List<BookEntity> getAll();

	ListResponse<BookSearchResultDTO> getAll(BookSearchReqDTO req);
}
