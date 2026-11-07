package com.aptech.boutquetShop.service.impl;

import com.aptech.boutquetShop.dto.MessageDTO;
import com.aptech.boutquetShop.dto.OccasionDTO;
import com.aptech.boutquetShop.entities.Messages;
import com.aptech.boutquetShop.entities.Occasions;
import com.aptech.boutquetShop.repositories.MessagesRepository;
import com.aptech.boutquetShop.repositories.OccasionsRepository;
import com.aptech.boutquetShop.utils.GenerateResponse;
import com.aptech.boutquetShop.utils.ListResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagesService {

	@Autowired
	private MessagesRepository messagesRepository;

	@Autowired
	private ModelMapper mapper;

	public ListResponse<MessageDTO.MessageSearchResponseDTO> getAllMessage() {
		try {
			List<Messages> messages = messagesRepository.findAll();
			List<MessageDTO.MessageSearchResponseDTO> listResponse = messages.stream().map(this::convertToObjectDto).collect(Collectors.toList());
			return GenerateResponse.generateSuccessListResponse(listResponse);
		} catch (Exception e) {
			return GenerateResponse.generateErrorListResponse("SQL Error Syntax!");
		}

	}

	private MessageDTO.MessageSearchResponseDTO convertToObjectDto(Object o) {
		return mapper.map(o, MessageDTO.MessageSearchResponseDTO.class);
	}

}
