package com.aptech.boutquetShop.api.controllers;


import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.dto.CartDTO;
import com.aptech.boutquetShop.dto.UserAccountDTO;
import com.aptech.boutquetShop.entities.CartItems;
import com.aptech.boutquetShop.service.impl.AuthService;
import com.aptech.boutquetShop.service.impl.BouquetService;
import com.aptech.boutquetShop.service.impl.CartService;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/cart")
public class CartController {

	@Autowired
	CartService cartService;

	@GetMapping
	public ListResponse<CartDTO.CartItemResponse> reqGetCartListByLoginUser() {
		return cartService.getCartItemList();
	}

	@PostMapping("/update")
	public BaseResponse<CartItems> reqUpdateCartItem(CartDTO.CartItemUpdateReqDTO req) {
		BaseResponse<CartItems> response = cartService.updateCartItem(req);
		return response;
	}

	@PostMapping("/delete")
	public BaseResponse<CartItems> reqDeleteCartItem(CartDTO.CartItemUpdateReqDTO req) {
		req.setQuantity(0);
		BaseResponse<CartItems> response = cartService.updateCartItem(req);
		return response;
	}



}
