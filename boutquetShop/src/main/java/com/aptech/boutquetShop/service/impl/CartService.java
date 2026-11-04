package com.aptech.boutquetShop.service.impl;

import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.dto.CartDTO;
import com.aptech.boutquetShop.dto.UserAccountDTO;
import com.aptech.boutquetShop.entities.Bouquets;
import com.aptech.boutquetShop.entities.CartItems;
import com.aptech.boutquetShop.entities.Carts;
import com.aptech.boutquetShop.repositories.BouquetRepository;
import com.aptech.boutquetShop.repositories.CartItemsRepository;
import com.aptech.boutquetShop.repositories.CartRepository;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.GenerateResponse;
import com.aptech.boutquetShop.utils.ListResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

	@Value("${upload.image.rootPath}")
	private String imgRoothPath;

	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	private ModelMapper mapper;
    @Autowired
    private BouquetRepository bouquetRepository;

	public ListResponse<CartDTO.CartItemResponse> getCartItemList() {
		try {
			UserAccountDTO.UserInforResponseDTO userInfor = authService.getUserInforFromJWT();
			Optional<Carts> cartInfor = cartRepository.findByUserId(userInfor.getId());

			if(!cartInfor.isPresent()){
				return GenerateResponse.generateSuccessListResponse(new ArrayList<CartDTO.CartItemResponse>());
			}

			CartDTO.CartItemSearchReqDTO req = new CartDTO.CartItemSearchReqDTO();
			req.setCartId(cartInfor.get().getId().toString());
			List<CartDTO.CartItemSearchResultDTO> cartItems = cartItemsRepository.getCartItemList(req);
			List<CartDTO.CartItemResponse> listResponse = cartItems.stream().map(this::convertToObjectDto).collect(Collectors.toList());

			listResponse.forEach(cartItem -> {
				String imgUrl = cartItem.getImageUrl();
				cartItem.setImageUrl(imgRoothPath+imgUrl);
			});
			return GenerateResponse.generateSuccessListResponse(listResponse);
		} catch (Exception e) {
			return GenerateResponse.generateErrorListResponse("SQL Error Syntax!");
		}

	}

	public BaseResponse<CartItems> updateCartItem(CartDTO.CartItemUpdateReqDTO req){
		try {
			UserAccountDTO.UserInforResponseDTO userInfor = authService.getUserInforFromJWT();
			Optional<Carts> cartInfor = cartRepository.findByUserId(userInfor.getId());
			Carts cartEntity ;
			if(!cartInfor.isPresent()){
				cartEntity = new Carts();
				cartEntity.setUserId(userInfor.getId());
				cartEntity = cartRepository.saveAndFlush(cartEntity);
			} else {
				cartEntity = cartInfor.get();
			}

			Bouquets bouquetEntity = bouquetRepository.findById(req.getBouquetId()).get();

			Optional<CartItems> cartItemInfor = cartItemsRepository.findByCartIdAndBouquetId(cartEntity.getId(),req.getBouquetId());
			CartItems cartItemEntity;
			if(!cartItemInfor.isPresent()){
				cartItemEntity = new CartItems();
				cartItemEntity.setCartId(cartEntity.getId());
				cartItemEntity.setBouquetId(req.getBouquetId());
				cartItemEntity.setQuantity(req.getQuantity());
				cartItemEntity.setUnitPrice(bouquetEntity.getPrice());
				if(req.getQuantity() > 0){
					cartItemEntity = cartItemsRepository.saveAndFlush(cartItemEntity);
				}
			} else{
				cartItemEntity =  cartItemInfor.get();
				if(req.getQuantity() > 0) {
					cartItemEntity.setQuantity(req.getQuantity());
					cartItemEntity.setUnitPrice(bouquetEntity.getPrice());
					cartItemEntity = cartItemsRepository.saveAndFlush(cartItemEntity);
				} else{
					cartItemsRepository.delete(cartItemEntity);
				}
			}
			return GenerateResponse.generateSuccsesBaseResponse(cartItemEntity);
        } catch (Exception e) {
			String errMessage = e.toString();
            return GenerateResponse.generateErrBaseResponse(errMessage);
        }

	}

	private CartDTO.CartItemResponse convertToObjectDto(Object o) {
		return mapper.map(o,CartDTO.CartItemResponse.class);
	}

}
