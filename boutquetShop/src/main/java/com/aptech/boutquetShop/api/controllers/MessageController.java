package com.aptech.boutquetShop.api.controllers;


import com.aptech.boutquetShop.dto.MessageDTO;
import com.aptech.boutquetShop.service.impl.MessagesService;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/message")
public class MessageController {

	@Autowired
	MessagesService messagesService;

	@GetMapping(value = "/all")
	public ListResponse<MessageDTO.MessageSearchResponseDTO> reqGetAllMessage() {
		return messagesService.getAllMessage();
	}

}
