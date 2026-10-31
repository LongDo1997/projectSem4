package com.aptech.boutquetShop.api.controllers;


import com.aptech.boutquetShop.dto.BookDTO.BookSearchReqDTO;
import com.aptech.boutquetShop.dto.BookDTO.BookSearchResponseDTO;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/boutquet")
public class BoutquetController {
//	@Autowired
//	private BookService bookService;
//
//	@Autowired
//	private ImageService imageService;

	@GetMapping
	public ListResponse<BookSearchResponseDTO> getAll(BookSearchReqDTO req, Pageable pageable) {
		System.out.println("312321");
//		return bookService.getBookListPaging(req, pageable);
		return null;
	}


}
