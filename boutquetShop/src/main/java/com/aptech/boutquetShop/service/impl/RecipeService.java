package com.aptech.boutquetShop.service.impl;


public class RecipeService {
//	@Autowired
//	private RecipeRepository recipeRepository;
//
//	@Autowired
//	private ImageService imageService;
//
//	@Autowired
//	private ModelMapper mapper;
//
//	public ListResponse<RecipeDTO> getAll(RecipeDTO req) {
//		try {
//			Pageable pageable = PageRequest.of(req.getPage() > 0 ? req.getPage() - 1 : 0, req.getSize() > 0 ? req.getSize() : 20);
//			Page<RecipeDTO.RecipeView> pageRecipe = recipeRepository.getAll(req,pageable);
//
////			listResponse.forEach(book -> {
////				book.setImageListDTO(imageService.getImagesById(book));
////			});
//
//			List<RecipeDTO> lstRecipeDTO = ObjectMapperUtils.mapAll(pageRecipe.getContent(), RecipeDTO.class);
//
//			return GenerateResponse.generateSuccessListResponse(lstRecipeDTO,pageRecipe.getTotalElements());
//
//		} catch (Exception e) {
//			return GenerateResponse.generateErrorListResponse("SQL Error Syntax!");
//		}
//
//	}

//
//	public BookEntity createBook(BookInsertReqDTO bookInsertReqDTO) throws Exception {
//		BookEntity book = mapper.map(bookInsertReqDTO, BookEntity.class);
//
//		BookEntity bookExist = bookRepository.findByNameAndAuthor(bookInsertReqDTO.getName(), bookInsertReqDTO.getAuthor());
//
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//		book.setId(null);
//		book.setCreatedAt(df.parse(Constants.getCurrentDateString()));
//		if (bookExist != null) {
//			Date current = df.parse(Constants.getCurrentDateString());
//			Date valiToDt = df.parse(bookExist.getValiToDt());
//
//			if (!valiToDt.before(current)) {
//				throw new Exception("Book Already Exists");
//			}
//
//			book.setId(bookExist.getId());
//			book.setValiToDt(Constants.MAXIMUM_DATE_STRING);
//			book.setCreatedAt(bookExist.getCreatedAt());
//		}
//		try {
//			return bookRepository.save(book);
//		} catch (Exception e) {
//			throw new Exception("Insert new Book Fail!");
//		}
//
//	}
//
//	public BookEntity updateBook(BookInsertReqDTO bookInsertReqDTO) throws Exception {
//		BookEntity bookEntity;
//		BookEntity book = mapper.map(bookInsertReqDTO, BookEntity.class);
//		BookEntity bookExist = bookRepository.findByIdAndValiToDtGreaterThanEqual(bookInsertReqDTO.getId(), Constants.getCurrentDateString());
//
//		if (bookExist == null) {
//			throw new Exception("Book is not Exist!");
//		}
//
//		book.setCreatedAt(bookExist.getCreatedAt());
//
//		try {
//			bookEntity = bookRepository.save(book);
//
//		} catch (Exception e) {
//			throw new Exception("Update Book Fail!");
//		}
//
//		return bookEntity;
//	}
//
//	public void deleteBook(Integer id) throws Exception {
//
//		BookEntity book = bookRepository.findByIdAndValiToDtGreaterThanEqual(id, Constants.getCurrentDateString());
//
//		if (book == null) {
//			throw new Exception("Book is not Exist!");
//		}
//
//		book.setValiToDt(Constants.getCurrentDateString());
//
//		try {
//			bookRepository.save(book);
//
//		} catch (Exception e) {
//			throw new Exception("Delete Book Fail!");
//		}
//	}
//
//	public void updateStatus(Integer id) throws Exception {
//		BookEntity book = bookRepository.findByIdAndValiToDtGreaterThanEqual(id, Constants.getCurrentDateString());
//		if (book == null) {
//			throw new Exception("Book is not Exist!");
//		}
//		try {
//			book.setStatus(0);
//			bookRepository.save(book);
//		} catch (Exception e) {
//			throw new Exception("Delete Book Fail!");
//		}
//	}
//
//	public BookEntity findById(Integer id) throws Exception {
//		Optional<BookEntity> bookEntityOptional = bookRepository.findById(id);
//		BookEntity bookEntity;
//		if (!bookEntityOptional.isPresent()) {
//			throw new Exception("Id is not exist");
//		}
//		bookEntity = bookEntityOptional.get();
//		return  bookEntity;
//	}

//	private RecipeDTO convertToObjectDto(Object o) {
//		return mapper.map(o, RecipeDTO.class);
//	}

}
