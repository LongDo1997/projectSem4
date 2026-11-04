package com.aptech.boutquetShop.api.controllers;


import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.service.impl.BouquetService;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/bouquet")
public class BouquetController {

	@Autowired
	BouquetService bouquetService;

	@GetMapping
	public ListResponse<BouquetDTO.BouquetSearchResponseDTO> reqGetBouquetListPaging(BouquetDTO.BouquetSearchReqDTO req, Pageable pageable) {
		return bouquetService.getBouquetListPaging(req, pageable);
	}

	@GetMapping(value = "/detail/{id}")
	public BaseResponse reqGetBouqetDetail(@PathVariable(name = "id") Integer id) throws Exception {
		return bouquetService.getBouquetListDetail(id);
	}


}
