package com.aptech.boutquetShop.service.impl;

public class CategoryService {
//	@Autowired
//	private CategoryRepository categoryRepository;
//
//	@Autowired
//	private ModelMapper mapper;
//
//	@Transactional
//	public ListResponse<CategoryEntity> getAll(CategoryDTO dto) {
//		try {
//			Pageable pageable = PageRequest.of(dto.getPage() > 0 ? dto.getPage() - 1 : 0, dto.getSize() > 0 ? dto.getSize() : 20);
//			Page<CategoryEntity> page = categoryRepository.getAll(dto.getName(), pageable);
//			return GenerateResponse.generateSuccessListResponse(page.getContent(), page.getTotalElements());
//		} catch (Exception e) {
//			return GenerateResponse.generateErrorListResponse("");
//		}
//	}
//
//	@Transactional
//	public CategoryEntity createCategory(CategoryDTO dto) throws Exception {
//		CategoryEntity categoryEntity = mapper.map(dto, CategoryEntity.class);
//		try {
//			return categoryRepository.save(categoryEntity);
//		} catch (Exception e) {
//			throw new Exception("Insert new Cat Fail!");
//		}
//	}
//
//	public CategoryEntity updateCat(CategoryDTO dto) throws Exception {
//		CategoryEntity entity;
//		CategoryEntity cat = mapper.map(dto, CategoryEntity.class);
//		CategoryEntity catExist = categoryRepository.findAllById(dto.getId());
//
//		if (catExist == null) {
//			throw new Exception("Category is not Exist!");
//		}
//		cat.setUpdatedAt(new Date());
//		try {
//			entity = categoryRepository.save(cat);
//		} catch (Exception e) {
//			throw new Exception("Update Category Fail!");
//		}
//		return entity;
//	}
//
////
////	public void deleteBook(Integer id) throws Exception {
////
////		BookEntity book = bookRepository.findByIdAndValiToDtGreaterThanEqual(id, Constants.getCurrentDateString());
////
////		if (book == null) {
////			throw new Exception("Book is not Exist!");
////		}
////
////		book.setValiToDt(Constants.getCurrentDateString());
////
////		try {
////			bookRepository.save(book);
////
////		} catch (Exception e) {
////			throw new Exception("Delete Book Fail!");
////		}
////	}
////
//	public void updateStatus(Integer id) throws Exception {
//		CategoryEntity cat = categoryRepository.findAllById(id);
//		if (cat == null) {
//			throw new Exception("Category not Exist!");
//		}
//		try {
//			cat.setStatus(0);
//			categoryRepository.save(cat);
//		} catch (Exception e) {
//			throw new Exception("Delete Category Fail!");
//		}
//	}
//
//	public CategoryEntity findById(Integer id) throws Exception {
//		Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);
//		CategoryEntity categoryEntity;
//		if (!categoryEntityOptional.isPresent()) {
//			throw new Exception("Id is not exist");
//		}
//		categoryEntity = categoryEntityOptional.get();
//		return categoryEntity;
//	}
//
//	private BookSearchResponseDTO convertToObjectDto(Object o) {
//		return mapper.map(o, BookSearchResponseDTO.class);
//	}

}
