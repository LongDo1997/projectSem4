package com.aptech.boutquetShop.service.impl;

import com.aptech.boutquetShop.dto.CartDTO;
import com.aptech.boutquetShop.dto.OrderDTO;
import com.aptech.boutquetShop.dto.UserAccountDTO;
import com.aptech.boutquetShop.entities.*;
import com.aptech.boutquetShop.repositories.*;
import com.aptech.boutquetShop.utils.BaseResponse;
import com.aptech.boutquetShop.utils.GenerateResponse;
import com.aptech.boutquetShop.utils.ListResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

	@Value("${upload.image.rootPath}")
	private String imgRoothPath;

	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	OrderRepository ordersRepository;

	@Autowired
	OrderItemsRepository orderItemsRepository;

	@Autowired
	RecipientsRepository recipientsRepository;

	@Autowired
	MessagesRepository messagesRepositoryl;

	@Autowired
	private ModelMapper mapper;

    @Autowired
    private BouquetRepository bouquetRepository;

	public ListResponse<OrderDTO.OrderListDTO> getOrderListByUser() {
		List<OrderDTO.OrderListDTO> listResponse = new ArrayList<OrderDTO.OrderListDTO>();
		try {

			UserAccountDTO.UserInforResponseDTO userInfor = authService.getUserInforFromJWT();
			List<Orders> orders = ordersRepository.findByUserId(userInfor.getId());
			if(CollectionUtils.isEmpty(orders)){
				return GenerateResponse.generateSuccessListResponse(listResponse);
			}
			listResponse = orders.stream().map(this::convertToOrderListDto).collect(Collectors.toList());
			listResponse.forEach(order -> {
				String orderStaus = order.getStatus();
				String orderStatusName = "";
				switch (orderStaus) {
					case "OC":
						orderStatusName = "Order Created";
						break;
					case "OB":
						orderStatusName = "Order Booked";
						break;
					case "OT":
						orderStatusName = "Transporting";
						break;
					case "OS":
						orderStatusName = "Order Success";
						break;
					case "CO":
						orderStatusName = "Order Cancelled";
					default:
						break;
				}
				order.setStatusName(orderStatusName);
				String paymentStatusNm = "Y".equals(order.getPaymentStatus()) ? "Paid Done" : "Not Pay Yet";
				order.setPaymentStatusName(paymentStatusNm);
			});
			return GenerateResponse.generateSuccessListResponse(listResponse);
		} catch (Exception e) {
			return GenerateResponse.generateErrorListResponse("SQL Error Syntax!");
		}

	}

	public BaseResponse<OrderDTO.OrderDetailResponse> getOrderDetail(Integer orderId) {
		Orders order = new Orders();
		try {
			// Select Order
			UserAccountDTO.UserInforResponseDTO userInfor = authService.getUserInforFromJWT();
			order = ordersRepository.findByIdAndUserId(orderId,userInfor.getId()).orElse(null);
			if (order == null){
				return GenerateResponse.generateErrBaseResponse("Order is not exist for this account");
			}
			OrderDTO.OrderDetailInforResponse orderDetailInforResponse = this.convertToOrderDetailInforResponse(order);
			String orderStaus = orderDetailInforResponse.getStatus();
			String orderStatusName = "";
			switch (orderStaus) {
				case "OC":
					orderStatusName = "Order Created";
					break;
				case "OB":
					orderStatusName = "Order Booked";
					break;
				case "OT":
					orderStatusName = "Transporting";
					break;
				case "OS":
					orderStatusName = "Order Success";
					break;
				case "CO":
					orderStatusName = "Order Cancelled";
				default:
					break;
			}
			orderDetailInforResponse.setStatusName(orderStatusName);
			String paymentStatusNm = "Y".equals(orderDetailInforResponse.getPaymentStatus()) ? "Paid Done" : "Not Pay Yet";
			orderDetailInforResponse.setPaymentStatusName(paymentStatusNm);

			// Select recipient of order
			Recipients recipient = recipientsRepository.findByOrderId(orderId).orElse(new Recipients());
			OrderDTO.OrderRecipientInforResponse orderRecipientInforResponse = this.convertToOrderRecipientInforResponse(recipient);

			// Select Order Item List from order Id
			OrderDTO.OrderItemReq ordItmReq= new OrderDTO.OrderItemReq();
			ordItmReq.setOrderId(orderId.toString());
			List<OrderDTO.OrderItemSearchResultDTO> orderItems = orderItemsRepository.getOrderItemList(ordItmReq);
			List<OrderDTO.OrderItemResponse> orderItemsResponse = new ArrayList<>();
			if(!CollectionUtils.isEmpty(orderItems)){
				orderItemsResponse = orderItems.stream().map(this::convertToOrderItemResponse).collect(Collectors.toList());
			}
			orderItemsResponse.forEach(item -> {
				item.setImageUrl(resolveImageUrl(item.getImageUrl()));
			});


			//Set Response object
			OrderDTO.OrderDetailResponse response = new OrderDTO.OrderDetailResponse();
			response.setOrderDetailInfor(orderDetailInforResponse);
			response.setOrderRecipientInfor(orderRecipientInforResponse);
			response.setOrderItems(orderItemsResponse);
			return GenerateResponse.generateSuccsesBaseResponse(response);
		} catch (Exception e) {
			String errMessage = e.toString();
			return GenerateResponse.generateErrBaseResponse(errMessage);
		}
	}

	public BaseResponse<Orders> createOrderFromCart(){
		Orders orderCreate = new Orders();
		try {
			UserAccountDTO.UserInforResponseDTO userInfor = authService.getUserInforFromJWT();
			Optional<Carts> cartInfor = cartRepository.findByUserId(userInfor.getId());
			Carts cartEntity ;
			List<CartItems> cartItems;

			if(!cartInfor.isPresent()){
				return GenerateResponse.generateErrBaseResponse("Cart is empty, Create Order Fail");
			} else {
				cartEntity = cartInfor.get();
				cartItems = cartItemsRepository.findByCartId(cartEntity.getId());
			}

			if(CollectionUtils.isEmpty(cartItems)){
				return GenerateResponse.generateErrBaseResponse("Cart is empty, Create Order Fail");
			}

			orderCreate = this.convertToOrderEntity(cartEntity);
			double totalAmount = cartItems.stream().mapToDouble(item -> item.getQuantity()*item.getUnitPrice()).sum();
			orderCreate.setStatus("OC");
			orderCreate.setPaymentMethod("Credit Card");
			orderCreate.setPaymentStatus("N");
			orderCreate.setTotalAmount(totalAmount);
			orderCreate = ordersRepository.saveAndFlush(orderCreate);
			Integer orderIdCreated = orderCreate.getId();

			List<OrderItems> orderItemsCreate = cartItems.stream().map(this::convertToOrderItemEntity).collect(Collectors.toList());
			orderItemsCreate.stream().forEach(item ->{
				item.setId(null);
				item.setOrderId(orderIdCreated);
			});
			orderItemsRepository.saveAll(orderItemsCreate);

			return GenerateResponse.generateSuccsesBaseResponse(orderCreate);
        } catch (Exception e) {
			if(orderCreate.getId() != null){
				ordersRepository.delete(orderCreate);
			}
			String errMessage = e.toString();
            return GenerateResponse.generateErrBaseResponse(errMessage);
        }

	}

	private String resolveImageUrl(String imageUrl) {
		if (imageUrl == null || imageUrl.trim().isEmpty()
				|| imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
			return imageUrl;
		}

		List<String> imgList = Arrays.asList(imageUrl.split(","));
		if(!CollectionUtils.isEmpty(imgList)){
			imgList = imgList.stream().map(img -> imgRoothPath + (img.startsWith("/") ? "" : "/") + img).collect(Collectors.toList());
		}
		return String.join(",",imgList);
	}

	private OrderDTO.OrderListDTO convertToOrderListDto(Object o) {
		return mapper.map(o,OrderDTO.OrderListDTO.class);
	}

	private Orders convertToOrderEntity(Object o) {
		return mapper.map(o,Orders.class);
	}

	private OrderItems convertToOrderItemEntity(Object o) {
		return mapper.map(o,OrderItems.class);
	}

	private OrderDTO.OrderDetailInforResponse convertToOrderDetailInforResponse(Object o) {
		return mapper.map(o,OrderDTO.OrderDetailInforResponse.class);
	}

	private OrderDTO.OrderRecipientInforResponse convertToOrderRecipientInforResponse(Object o) {
		return mapper.map(o,OrderDTO.OrderRecipientInforResponse.class);
	}

	private OrderDTO.OrderItemResponse convertToOrderItemResponse(Object o) {
		return mapper.map(o,OrderDTO.OrderItemResponse.class);
	}


}
