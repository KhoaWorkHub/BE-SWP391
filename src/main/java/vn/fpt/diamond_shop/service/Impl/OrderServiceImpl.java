package vn.fpt.diamond_shop.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.fpt.diamond_shop.constants.StatusOrder;
import vn.fpt.diamond_shop.model.Cart;
import vn.fpt.diamond_shop.model.Jewelry;
import vn.fpt.diamond_shop.model.OrderDetail;
import vn.fpt.diamond_shop.repository.*;
import vn.fpt.diamond_shop.request.AddCartRequest;
import vn.fpt.diamond_shop.request.AddOrderRequest;
import vn.fpt.diamond_shop.request.GetListCartRequest;
import vn.fpt.diamond_shop.request.GetListOrderRequest;
import vn.fpt.diamond_shop.response.*;
import vn.fpt.diamond_shop.security.UserPrincipal;
import vn.fpt.diamond_shop.service.OrderService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private JewelryRepository jewelryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ImageRepository imageRepository;
    @Override
    public ResponseEntity<Object> orderList(GetListOrderRequest request) {
        List<GetListOrderResponse>orderResponses = new ArrayList<>();
        if(request.getLimit() == null){
            request.setLimit(10);
        }
        if(request.getOffset() == null){
            request.setOffset(0);
        }
        Page<GetListOrderResponse> orderList =  null;
        orderResponses = orderList.getContent();
        Meta meta = new Meta(request.getRequestId(), 200, "success", HttpStatus.OK.toString());
        meta.setLimit(request.getLimit());
        meta.setOffset(request.getOffset());
        meta.setTotal(Integer.valueOf(String.valueOf(orderList.getTotalElements()))) ;
        BaseResponse response = new BaseResponse(meta,orderResponses);

        return ResponseEntity.ok(response);
    }

    @Override
    public AddOrderResponse addOrder(AddOrderRequest request) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDate(new Date(new java.util.Date().getTime()));
        orderDetail.setCreatedAt(new Date(new java.util.Date().getTime()));
        orderDetail.setJewelryId(request.getJewelryId());
        orderDetail.setCustomerId(request.getCustomerId());
        orderDetail.setStatus(StatusOrder.INIT.getCode());
        orderDetail.setQuantityNumber(request.getQuantity());
        Optional<Jewelry> Jewelry = jewelryRepository.findById(request.getJewelryId());
        Jewelry jewelryData = Jewelry.get();
        if(jewelryData != null){
            orderDetail.setTotalPrice(request.getQuantity()*jewelryData.getMaterialPrices());
        }
        orderDetailRepository.save(orderDetail);
        AddOrderResponse response = new AddOrderResponse();
        BeanUtils.copyProperties(orderDetail, response);
        response.setJewelryTitle(jewelryData.getName());
        response.setPriceItems(jewelryData.getMaterialPrices());
        response.setTotalPrice(orderDetail.getTotalPrice());
        response.setImageUrl(imageRepository.getById(jewelryData.getImageId()).getUrl());
        return response;
    }

    @Override
    public Object listCart(GetListCartRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cartRepository.getListCartResponse(userPrincipal.getId());
    }

    @Override
    public Boolean addCart(AddCartRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = new Cart();
        cart.setJewelryId(request.getJewelryId());
        cart.setQuantity(request.getQuantity());
        cart.setUserId(userPrincipal.getId());
        cart.setCreatedAt(new Date(new java.util.Date().getTime()));
        cartRepository.save(cart);
        return true;
    }
}
