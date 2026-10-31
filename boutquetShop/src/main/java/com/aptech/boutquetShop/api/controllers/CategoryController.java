package com.aptech.boutquetShop.api.controllers;

import com.aptech.boutquetShop.dto.CategoryDTO;
import com.aptech.boutquetShop.entities.CategoryEntity;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/category")
public class CategoryController {
//	@Autowired
//	private CategoryService categoryService;

	@GetMapping
	public ListResponse<CategoryEntity> getAll(CategoryDTO dto) {

//		return categoryService.getAll(dto);
		return null;
	}

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseResponse createCategory(@RequestBody CategoryDTO dto) {
//		try {
//			// Insert Cat
//			return GenerateResponse.generateSuccsesBaseResponse(categoryService.createCategory(dto));
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
		return null;
	}

	@GetMapping(value = "/{id}")
	public BaseResponse detail(@PathVariable(name = "id") Integer id) throws Exception {
//		return GenerateResponse.generateSuccsesBaseResponse(categoryService.findById(id));
		return null;
	}

	@PostMapping(value = "/update", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE } )
	public BaseResponse updateCat(CategoryDTO dto) {
//		try {
//			// Insert Cat
//			return GenerateResponse.generateSuccsesBaseResponse(categoryService.updateCat(dto));
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
		return null;
	}

	@DeleteMapping("/{id}")
	public BaseResponse deleteCategory(@PathVariable("id") Integer id) {
//		try {
//			categoryService.updateStatus(id);
//			return GenerateResponse.generateSuccsesBaseResponse(null);
//		} catch (Exception e) {
//			return GenerateResponse.generateErrBaseResponse(e.getMessage());
//		}
		return null;
	}

}
