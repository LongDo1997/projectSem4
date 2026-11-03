package com.aptech.boutquetShop.service.impl;

import com.aptech.boutquetShop.dto.BouquetDTO;
import com.aptech.boutquetShop.dto.OccasionDTO;
import com.aptech.boutquetShop.entities.Occasions;
import com.aptech.boutquetShop.repositories.BouquetRepository;
import com.aptech.boutquetShop.repositories.OccasionsRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OccasionService {

	@Autowired
	private OccasionsRepository occasionsRepository;

	@Autowired
	private ModelMapper mapper;

	public ListResponse<OccasionDTO.OccasionSearchResponseDTO> getAllOccasion() {
		try {
			List<Occasions> occasions = occasionsRepository.findAll();
			List<OccasionDTO.OccasionSearchResponseDTO> listResponse = occasions.stream().map(this::convertToObjectDto).collect(Collectors.toList());
			return GenerateResponse.generateSuccessListResponse(listResponse);
		} catch (Exception e) {
			return GenerateResponse.generateErrorListResponse("SQL Error Syntax!");
		}

	}

	private OccasionDTO.OccasionSearchResponseDTO convertToObjectDto(Object o) {
		return mapper.map(o, OccasionDTO.OccasionSearchResponseDTO.class);
	}

}
