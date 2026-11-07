package com.aptech.boutquetShop.service.impl;

import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.repositories.BouquetRepository;
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

@Service
public class BouquetService {

	@Value("${upload.image.rootPath}")
	private String imgRoothPath;

	@Autowired
	private BouquetRepository bouquetRepository;

	@Autowired
	private ModelMapper mapper;

	public ListResponse<BouquetDTO.BouquetSearchResponseDTO> getBouquetListPaging(BouquetDTO.BouquetSearchReqDTO req, Pageable pageable) {
		try {
			Integer pageSize =  req.getSize() > 0 ? req.getSize() : 20;
			Integer pageNum = req.getPage() > 0 ? req.getPage() - 1 : 0;
			Pageable pageable1 = PageRequest.of(pageNum, pageSize);
			req.setActive(true);
			Page<BouquetDTO.BouquetSearchResultDTO> listBouqet = bouquetRepository.getBouquetListPaing(req, pageable1);
			Page<BouquetDTO.BouquetSearchResponseDTO> listResponse = listBouqet.map(this::convertToObjectDto);
			listResponse.forEach(bouquet -> {
				String imgUrl = bouquet.getImageUrl();
				bouquet.setImageUrl(imgRoothPath+imgUrl);
			});
			return GenerateResponse.generateSuccessListResponse(listResponse.getContent(), listResponse.getTotalElements(), listResponse.getTotalPages(), pageSize, pageNum +1);
		} catch (Exception e) {
			return GenerateResponse.generateErrorListResponse("SQL Error Syntax!");
		}

	}

	public BaseResponse<BouquetDTO.BouquetSearchResponseDTO> getBouquetListDetail(Integer id) throws Exception {
		BouquetDTO.BouquetSearchReqDTO req = new BouquetDTO.BouquetSearchReqDTO();
		if(id == null){
			return GenerateResponse.generateErrBaseResponse("id is not valid");
		}
		req.setId(id.toString());
		req.setActive(true);
		BouquetDTO.BouquetSearchResultDTO bouqetDetail = bouquetRepository.getBouquetDetail(req);
		if (bouqetDetail == null) {
			return GenerateResponse.generateErrBaseResponse("Product is not exist");
		}
		BouquetDTO.BouquetSearchResponseDTO response = convertToObjectDto(bouqetDetail);
		response.setImageUrl(resolveImageUrl(response.getImageUrl()));
		return GenerateResponse.generateSuccsesBaseResponse(response);
	}

	private String resolveImageUrl(String imageUrl) {
		if (imageUrl == null || imageUrl.trim().isEmpty()
				|| imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
			return imageUrl;
		}
		return imgRoothPath + (imageUrl.startsWith("/") ? "" : "/") + imageUrl;
	}


	private BouquetDTO.BouquetSearchResponseDTO convertToObjectDto(Object o) {
		return mapper.map(o, BouquetDTO.BouquetSearchResponseDTO.class);
	}

}
