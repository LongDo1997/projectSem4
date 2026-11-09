package com.aptech.boutquetShop.api.controllers;


import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.dto.OccasionDTO;
import com.aptech.boutquetShop.service.impl.BouquetService;
import com.aptech.boutquetShop.service.impl.OccasionService;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/occasion")
public class OccasionController {

	@Autowired
	OccasionService occasionService;

	@GetMapping(value = {"/all","/anonymous/all"})
	public ListResponse<OccasionDTO.OccasionSearchResponseDTO> reqGetAllOccasion() {
		return occasionService.getAllOccasion();
	}

}
