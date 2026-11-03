package com.aptech.boutquetShop.api.controllers;


import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.service.impl.BouquetService;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/cart")
public class CartController {

	@Autowired
	BouquetService bouquetService;

	@GetMapping
	public ListResponse<BouquetDTO.BouquetSearchResponseDTO> reqGetBouquetListPaging(BouquetDTO.BouquetSearchReqDTO req, Pageable pageable) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("123123123");
		System.out.println(authentication);

		System.out.println(authentication.getDetails());
		System.out.println(authentication.getPrincipal());
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		System.out.println(userDetails.getUsername());
		return bouquetService.getBouquetListPaging(req, pageable);
	}

	@GetMapping(value = "/detail/{id}")
	public BaseResponse reqGetBouqetDetail(@PathVariable(name = "id") Integer id) throws Exception {
		return bouquetService.getBouquetListDetail(id);
	}


}
