package com.aptech.boutquetShop.api.controllers;

import com.aptech.boutquetShop.dto.RecipeDTO;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/recipe")
public class RecipeController {
//	@Autowired
//	private RecipeService recipeService;
//
//	@Autowired
//	private ImageService imageService;

	@GetMapping
	public ListResponse<RecipeDTO> getAll(@RequestBody RecipeDTO req) {
//		return recipeService.getAll(req);
		return null;
	}

//	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
//	public @ResponseBody BaseResponse createBook(BookInsertReqDTO bookInsertReqDTO) throws Exception {
//		try {
//			// Upload Image
//			List<String> imageIds = imageService.createImage(bookInsertReqDTO.getFile(), "Book");
//			bookInsertReqDTO.setImageList(String.join(",", imageIds));
//			// Insert Book
//			return GenerateResponse.generateSuccsesBaseResponse(bookService.createBook(bookInsertReqDTO));
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
//	}
//
//	@GetMapping(value = "/{id}")
//	public BaseResponse detail(@PathVariable(name = "id") Integer id) throws Exception {
//		return GenerateResponse.generateSuccsesBaseResponse(bookService.findById(id));
//	}
//
//	@PostMapping(value = "/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
//	public BaseResponse updateBook(BookInsertReqDTO dto) {
//		try {
//			// Upload Image
//			List<String> imageIds = imageService.createImage(dto.getFile(), "Book");
//			dto.setImageList(String.join(",", imageIds));
//			// Insert Book
//			return GenerateResponse.generateSuccsesBaseResponse(bookService.updateBook(dto));
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
//	}
//
//	@DeleteMapping("/{id}")
//	public BaseResponse deleteBook(@PathVariable("id") Integer id) {
//		try {
//			bookService.updateStatus(id);
//			return GenerateResponse.generateSuccsesBaseResponse(null);
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
//	}
}
