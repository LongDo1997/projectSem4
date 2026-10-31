package com.aptech.boutquetShop.api.controllers;


import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.aptech.boutquetShop.dto.BookDTO.BookInsertReqDTO;
import com.aptech.boutquetShop.dto.BookDTO.BookSearchReqDTO;
import com.aptech.boutquetShop.dto.BookDTO.BookSearchResponseDTO;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/book")
public class BookController {
//	@Autowired
//	private BookService bookService;
//
//	@Autowired
//	private ImageService imageService;

	@GetMapping
	public ListResponse<BookSearchResponseDTO> getAll(BookSearchReqDTO req, Pageable pageable) {
//		return bookService.getBookListPaging(req, pageable);
		return null;
	}

	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody BaseResponse createBook(BookInsertReqDTO bookInsertReqDTO) throws Exception {
//		try {
//			// Upload Image
//			List<String> imageIds = imageService.createImage(bookInsertReqDTO.getFile(), "Book");
//			bookInsertReqDTO.setImageList(String.join(",", imageIds));
//			// Insert Book
//			return GenerateResponse.generateSuccsesBaseResponse(bookService.createBook(bookInsertReqDTO));
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
		return null;
	}

	@GetMapping(value = "/{id}")
	public BaseResponse detail(@PathVariable(name = "id") Integer id) throws Exception {
//		return GenerateResponse.generateSuccsesBaseResponse(bookService.findById(id));
		return null;
	}

	@PostMapping(value = "/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
	public BaseResponse updateBook(BookInsertReqDTO dto) {
//		try {
//			// Upload Image
//			List<String> imageIds = imageService.createImage(dto.getFile(), "Book");
//			dto.setImageList(String.join(",", imageIds));
//			// Insert Book
//			return GenerateResponse.generateSuccsesBaseResponse(bookService.updateBook(dto));
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
		return null;
	}

	@DeleteMapping("/{id}")
	public BaseResponse deleteBook(@PathVariable("id") Integer id) {
//		try {
//			bookService.updateStatus(id);
//			return GenerateResponse.generateSuccsesBaseResponse(null);
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
		return null;
	}
}
