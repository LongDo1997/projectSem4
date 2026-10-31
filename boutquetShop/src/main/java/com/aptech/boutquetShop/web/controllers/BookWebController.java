package com.aptech.boutquetShop.web.controllers;

import com.aptech.boutquetShop.utils.Constants;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_WEB + "/book")
public class BookWebController {
//	@Autowired
//	private BookService bookService;
//
//	@GetMapping(value = { "", "/", "/index" })
//	public String index(Model model) {
//		BookDTO.BookSearchReqDTO req = new BookDTO.BookSearchReqDTO();
//		// ListResponse<BookEntity> bookList = bookService.getAll(req);
//
//		// model.addAttribute("books", bookList.getData().getItems());
//
//		return "web/admin/book/index";
//	}
}
