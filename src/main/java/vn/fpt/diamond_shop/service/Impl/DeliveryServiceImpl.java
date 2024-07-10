package vn.fpt.diamond_shop.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.fpt.diamond_shop.constants.StatusDelivery;
import vn.fpt.diamond_shop.constants.StatusOrder;
import vn.fpt.diamond_shop.exception.DiamondShopException;
import vn.fpt.diamond_shop.model.*;
import vn.fpt.diamond_shop.repository.*;
import vn.fpt.diamond_shop.request.*;
import vn.fpt.diamond_shop.response.*;
import vn.fpt.diamond_shop.service.DeliveryService;
import vn.fpt.diamond_shop.service.OrderService;
import vn.fpt.diamond_shop.util.UUIDUtil;

import java.util.*;

@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private DeliverRepository deliverRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public ResponseEntity<Object> listDelivery(GetListDeliveryRequest request) {
        List<Delivery> allByOrderByIdDesc = deliveryRepository.findAllByOrderByIdDesc();
        return ResponseEntity.ok(allByOrderByIdDesc);
    }

    @Override
    public ResponseEntity<Object> getListDeliver() {
        List<Deliver> allByOrderByIdDesc = deliverRepository.findAllByOrderByIdDesc();

        List<DeliverListResponse> deliverListResponses = new ArrayList<>();
        allByOrderByIdDesc.stream().forEach(
                allByOrderByIdDescDeliver -> {
                    DeliverListResponse deliverListResponse = new DeliverListResponse();
                    deliverListResponse.setId(allByOrderByIdDescDeliver.getId());
                    deliverListResponse.setUserId(allByOrderByIdDescDeliver.getUserId());
                    deliverListResponse.setTotalOrder(allByOrderByIdDescDeliver.getTotalOrder());
                    deliverListResponse.setTotalOrderFail(allByOrderByIdDescDeliver.getTotalOrderFail());
                    deliverListResponse.setTotalOrderSuccess(allByOrderByIdDescDeliver.getTotalOrderSuccess());
                    deliverListResponse.setStatus(allByOrderByIdDescDeliver.getStatus());
                    deliverListResponse.setUserName(endUserRepository.findEndUserByAccountId(allByOrderByIdDescDeliver.getUserId()).get().getFullName());
                    deliverListResponses.add(deliverListResponse);
                }
        );

        return ResponseEntity.ok(deliverListResponses);
    }

    @Override
    public Boolean updateDeliver(UpdateDeliverRequest request) {
        Deliver deliverByUserId = deliverRepository.findByUserId(request.getDeliverId());
        if (deliverByUserId != null) {
            if(!StringUtils.isEmpty(request.getStatus())){
                deliverByUserId.setStatus(request.getStatus());
            }
            if(request.getTotalOrder() != null){
                deliverByUserId.setTotalOrder(request.getTotalOrder());
            }
            if(request.getTotalOrderFail() != null){
                deliverByUserId.setTotalOrder(request.getTotalOrderFail());
            }
            if(request.getTotalOrderSuccess() != null){
                deliverByUserId.setTotalOrder(request.getTotalOrderSuccess());
            }
            deliverRepository.updateDeliverByUserId(deliverByUserId.getUserId(), deliverByUserId.getStatus(), deliverByUserId.getTotalOrder(), deliverByUserId.getTotalOrderSuccess(), deliverByUserId.getTotalOrderFail(), deliverByUserId.getUpdatedAt());
        }
        return true;

    }

    @Override
    public Boolean addDelivery(AddDeliveryRequest request) {
        Delivery allByOrderId = deliveryRepository.findAllByOrderId(request.getOrderId());
        Delivery delivery = new Delivery();
        List<Deliver> allDeleverByStatus = deliverRepository.findAllByStatus(StatusDelivery.StatusDeliver.ACTIVE.getValue());
        Random rand = new Random();
        Date date1 = new Date();
        Date date2 = (Date) date1.clone();
        date2.setDate(date1.getDate() + 3);
        if(allByOrderId == null){
            //add
            delivery.setDeliveryFee(request.getDeliveryId());
            delivery.setStatus(StatusDelivery.WAITING.getValue());
            delivery.setCreatedAt(new Date());
            delivery.setEndDateEstimated(request.getEndDateEstimated());
            delivery.setOrderId(request.getOrderId());
            delivery.setStatusDate(new Date());
            delivery.setEndDateEstimated(date2);
            if(request.getDeliveryId() == null){
                delivery.setDeliverId(allDeleverByStatus.get(rand.nextInt(allDeleverByStatus.size())).getUserId());
            }else{
                Deliver byId = deliverRepository.findById(request.getDeliveryId()).get();
                delivery.setDeliverId(byId.getUserId());
            }
            deliveryRepository.save(delivery);
        }else {
            //update
            if(request.getDeliveryId() != null){
                allByOrderId.setDeliverId(request.getDeliveryId());
            }
            allByOrderId.setEndDateEstimated(date2);
            allByOrderId.setUpdatedAt(new Date());
            allByOrderId.setDeliveryFee(request.getDeliveryFee() == null ? allByOrderId.getDeliveryFee() : request.getDeliveryFee());
            deliveryRepository.save(allByOrderId);
        }

        return true;
    }
}
