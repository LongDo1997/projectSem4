package com.aptech.boutquetShop.service.impl;

import com.aptech.boutquetShop.dto.OrderDTO;
import com.aptech.boutquetShop.dto.OrderProcessDTO;
import com.aptech.boutquetShop.dto.UserAccountDTO;
import com.aptech.boutquetShop.entities.*;
import com.aptech.boutquetShop.repositories.*;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.GenerateResponse;
import com.aptech.boutquetShop.utils.ListResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderProcessService {

	@Autowired
	private AuthService authService;


	@Autowired
	OrderRepository ordersRepository;

	@Autowired
	RecipientsRepository recipientsRepository;


	@Autowired
	private ModelMapper mapper;


	public BaseResponse<Orders> processPaymentByCreditCard(Integer orderId){
		Orders orderCreate = new Orders();
		try {
			UserAccountDTO.UserInforResponseDTO userInfor = authService.getUserInforFromJWT();
			Orders order = ordersRepository.findByIdAndUserId(orderId,userInfor.getId()).orElse(null);
			if (order == null){
				return GenerateResponse.generateErrBaseResponse("Order is not exist for this account");
			}
			if("Y".equals(order.getPaymentStatus())){
				return GenerateResponse.generateErrBaseResponse("Payment fail, please check your information again!");
			}
			order.setPaymentStatus("Y");
			order.setPaymentMethod("Credit Card");
			ordersRepository.saveAndFlush(order);

			return GenerateResponse.generateSuccsesBaseResponse(orderCreate);
        } catch (Exception e) {
			String errMessage = e.toString();
            return GenerateResponse.generateErrBaseResponse(errMessage);
        }
	}

	public BaseResponse<Orders> processBookingOrder(OrderProcessDTO.OrderBookingReqDTO req){
		Orders order = new Orders();
		Recipients recipients = null;
		try {
			UserAccountDTO.UserInforResponseDTO userInfor = authService.getUserInforFromJWT();
			order = ordersRepository.findByIdAndUserId(req.getOrderId(),userInfor.getId()).orElse(null);
			if (order == null){
				return GenerateResponse.generateErrBaseResponse("Order is not exist for this account");
			}
			if(!"OC".equals(order.getStatus())){
				return GenerateResponse.generateErrBaseResponse("Booking fail, please check your information again!");
			}
			String paymentMethod = req.getPaymentMethod();
			if(!"Credit Card".equals(paymentMethod) && !"By Cash".equals(paymentMethod)){
				return GenerateResponse.generateErrBaseResponse("Booking fail, Payment Method is invalid ('Credit Card'/'By Cash')n!");
			}
			// Save Recipient Infor
			recipients = this.convertToRecipientEntity(req.getReceipentReq());
			recipients.setOrderId(req.getOrderId());
			recipients = recipientsRepository.saveAndFlush(recipients);
			// Save Order status
			order.setDeliveryDate(this.getDeliveryTime());
			order.setStatus("OB");

			if(!"Y".equals(order.getPaymentStatus())){
				order.setPaymentMethod(req.getPaymentMethod());
			}
			ordersRepository.saveAndFlush(order);
			return GenerateResponse.generateSuccsesBaseResponse(order);
		} catch (Exception e) {
			if(recipients != null){
				recipientsRepository.delete(recipients);
			}
			String errMessage = e.toString();
			return GenerateResponse.generateErrBaseResponse(errMessage);
		}
	}


	private Date getDeliveryTime(){
		LocalDateTime now = LocalDateTime.now();
		int manualSeconds = (now.getHour() * 3600) + (now.getMinute() * 60) + now.getSecond();
		int nineAmSecs = 9 * 3600;
		int deliveryEndTimeSecs = 17 * 3600;

		LocalDateTime deliveryDt;

		if (manualSeconds < nineAmSecs) {
			deliveryDt = LocalDate.now().atTime(9 + 5, 0); // 5 hours after start working time (14:00)
		} else if (manualSeconds >= nineAmSecs && manualSeconds <= deliveryEndTimeSecs) {
			deliveryDt = now.plusHours(5); // 5 hours from current
		} else {
			deliveryDt = LocalDate.now().plusDays(1).atTime(9 + 5, 0); // 5 hours after start working time tomorrow (14:00)
		}
		return  Date.from(deliveryDt.atZone(ZoneId.systemDefault()).toInstant());
	}

	private Recipients convertToRecipientEntity(Object o) {
		return mapper.map(o,Recipients.class);
	}

}
