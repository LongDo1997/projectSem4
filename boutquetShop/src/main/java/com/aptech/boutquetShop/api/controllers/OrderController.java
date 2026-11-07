package com.aptech.boutquetShop.api.controllers;


import com.aptech.boutquetShop.dto.OrderDTO;
import com.aptech.boutquetShop.dto.OrderProcessDTO;
import com.aptech.boutquetShop.entities.Orders;
import com.aptech.boutquetShop.service.impl.OrderProcessService;
import com.aptech.boutquetShop.service.impl.OrderService;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.Constants;
import com.aptech.boutquetShop.utils.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX_API + Constants.VERSION_API_V1 + "/order")
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderProcessService orderProcessService;

	@GetMapping
	public ListResponse<OrderDTO.OrderListDTO> reqGetOrderListByLoginUser() {
		return orderService.getOrderListByUser();
	}

	@GetMapping(value = "/detail/{id}")
	public BaseResponse<OrderDTO.OrderDetailResponse> reqGetOrderDetail(@PathVariable(name = "id") Integer id)  {
		return orderService.getOrderDetail(id);
	}

	@PostMapping("/submit")
	public BaseResponse<Orders> reqSubmitOrderFromCart() {
		return orderService.createOrderFromCart();
	}

	@PostMapping("/edit/paid")
	public BaseResponse<Orders> reqUpdateOrderToPaid(Integer id) {
		return orderProcessService.processPayment(id);
	}

	@PostMapping("/edit/booking")
	public BaseResponse<Orders> reqUpdateOrderToBooking(@RequestBody OrderProcessDTO.OrderBookingReqDTO req) {
		return orderProcessService.processBookingOrder(req);
	}


}
