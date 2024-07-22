package vn.fpt.diamond_shop.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import vn.fpt.diamond_shop.security.AccountService;
import vn.fpt.diamond_shop.security.UserPrincipal;
import vn.fpt.diamond_shop.security.model.User;
import vn.fpt.diamond_shop.service.OrderService;
import vn.fpt.diamond_shop.util.DateTimeUtils;
import vn.fpt.diamond_shop.util.UUIDUtil;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Autowired
    private EndUserRepository endUserRepository;
    private static String ACTIVE_CART = "active";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private AccountService accountService;

    @Autowired
    private DeliverRepository deliverRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private DiamondRepository diamondRepository;

    @Override
    public ResponseEntity<Object> orderList(GetListOrderRequest request) {
        if (request.getLimit() == null) {
            request.setLimit(9);
        }
        if (request.getOffset() == null) {
            request.setOffset(0);
        }
        if (!StringUtils.isEmpty(request.getPhoneNumber())) {
            EndUser endUserByPhoneNumber = endUserRepository.findEndUserByPhoneNumber(request.getPhoneNumber());
            if (endUserByPhoneNumber != null) {
                request.setCustomerId(endUserByPhoneNumber.getAccountId());
            }
        }
        Page<Orders> ordersPage = null;
        if (StringUtils.isEmpty(request.getStatus())) {
            ordersPage = ordersRepository.findAllOrderByCustomerIdOrderByCreatedAtDesc(request.getCustomerId(), PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit(), Sort.by(Sort.Direction.DESC, "id")));
        }else{
            ordersPage = ordersRepository.findAllOrderByCustomerIdAndStatusOrderByCreatedAtDesc(request.getCustomerId(), request.getStatus(), PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit(), Sort.by(Sort.Direction.DESC, "id")));
        }

        Page<OrderDetail> orderDetailsPage = null;
        if (StringUtils.isEmpty(request.getStatus())) {
            orderDetailsPage = orderDetailRepository.findAllByCustomerIdOrderByCreatedAtDesc(request.getCustomerId(), PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit(), Sort.by(Sort.Direction.DESC, "id")));
        } else {
            orderDetailsPage = orderDetailRepository.findAllByCustomerIdAndStatusOrderByCreatedAtDesc(request.getCustomerId(), request.getStatus(), PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit(), Sort.by(Sort.Direction.DESC, "id")));
        }
        List<OrdersListAllUser> ordersListAllUsers = new ArrayList<>();
        for (Orders order : ordersPage) {
            OrdersListAllUser ordersListAllUser = new OrdersListAllUser();
            BeanUtils.copyProperties(order, ordersListAllUser);
            List<OrderDetail> allByUniqueOrderId = orderDetailRepository.findAllByUniqueOrderId(order.getUniqueOrderId());
            ordersListAllUser.setOrderDetails(allByUniqueOrderId);
            ordersListAllUser.setDeliveryInfo(deliveryRepository.findAllByOrderId(order.getUniqueOrderId()));
            ordersListAllUser.setPhoneNumber(request.getPhoneNumber());
            ordersListAllUsers.add(ordersListAllUser);
        }
        Meta meta = new Meta(request.getRequestId(), 200, "success", HttpStatus.OK.toString());
        meta.setLimit(request.getLimit());
        meta.setOffset(request.getOffset());
        meta.setTotal(Integer.valueOf(String.valueOf(orderDetailsPage.getTotalElements())));
        BaseResponse response = new BaseResponse(meta, ordersListAllUsers);
        return ResponseEntity.ok(response);
    }

    @Override
    public AddOrderResponse addOrder(AddOrderRequest request) {
        List<Cart> cartsById = cartRepository.findAllById(request.getCartIds());
        EndUser endUser = endUserRepository.findEndUserByAccountId(request.getCustomerId()).get();
        User user = userRepository.findById(endUser.getAccountId()).get();
        String uniqueOrderId = UUIDUtil.generateUUID();
        List<Long> listJewelris = new ArrayList<>();
        Orders orders = new Orders();
        SendInvoiceRequest sendInvoiceRequest = new SendInvoiceRequest();
        List<SendInvoiceRequest.Products> products = new ArrayList<>();
        if (!cartsById.isEmpty()) {
            Long priceItems = 0L;
            for (Cart cart : cartsById) {
                //insert order detail
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderDate(new java.util.Date());
                orderDetail.setCreatedAt(new Date());
                orderDetail.setJewelryId(cart.getJewelryId());
                orderDetail.setCustomerId(cart.getUserId());
                orderDetail.setStatus(StatusOrder.CREATE_PAYMENT.getValue());
                orderDetail.setUniqueOrderId(uniqueOrderId);
                orderDetail.setQuantityNumber(cart.getQuantity());
                orderDetail.setSaleId(cart.getSaleId());
                orderDetail.setSize(cart.getSize());
                Optional<Jewelry> Jewelry = jewelryRepository.findById(cart.getJewelryId());
                Jewelry jewelryData = Jewelry.get();
                if (jewelryData != null) {
                    SendInvoiceRequest.Products product = new SendInvoiceRequest.Products();
                    product.setName(jewelryData.getName());
                    product.setPrice(String.valueOf(jewelryData.getMaterialPrices()));
                    product.setQuantity(String.valueOf(product.getQuantity()));
                    product.setImage(imageRepository.findById(jewelryData.getImageId()).get().getUrl());
                    products.add(product);
                    orderDetail.setTotalPrice(cart.getQuantity() * jewelryData.getMaterialPrices());
                }
                priceItems += orderDetail.getTotalPrice();
                orderDetailRepository.save(orderDetail);
                listJewelris.add(cart.getJewelryId());
                cartRepository.deleteById(cart.getId());
                orders.setCustomerId(orderDetail.getCustomerId());
            }

            orders.setUniqueOrderId(uniqueOrderId);
            orders.setOrderDate(new Date());
            orders.setCreatedAt(new Date());
            orders.setStatus(StatusOrder.CREATE_PAYMENT.getValue());
            orders.setTotalPrice(priceItems);
            ordersRepository.save(orders);


            AddOrderResponse response = new AddOrderResponse();
            response.setTotalPrice(priceItems);
            response.setUniqueOrderId(uniqueOrderId);
            response.setJewelryId(listJewelris);

            sendInvoiceRequest.setOrderId(uniqueOrderId);
            sendInvoiceRequest.setDate(DateTimeUtils.format(new Date(), "dd/MM/yyyy HH:mm:ss"));
            sendInvoiceRequest.setMail(user.getEmail());
            sendInvoiceRequest.setCustomerId(request.getCustomerId());
            sendInvoiceRequest.setTotal(String.valueOf(orders.getTotalPrice()));
            sendInvoiceRequest.setProducts(products);
            // tam thoi chay code
            sendInvoiceRequest.setAddress("tam thoi chay code");
            addInvoice(sendInvoiceRequest);

            return response;
        } else {
            throw new DiamondShopException(400, "Dont exist cart info!");
        }
    }

    @Override
    public Object listCart(GetListCartRequest request) {
        if (!StringUtils.isEmpty(request.getPhoneNumber())) {
            List<ListCartResponse> listCartResponse = cartRepository.getListCartByPhoneNumberResponse(request.getPhoneNumber());
            return listCartResponse;
        } else {
            List<ListCartResponse> listCartResponse = cartRepository.getListCartResponse(request.getCustomerId());
            return listCartResponse;
        }
    }

    @Override
    public Boolean addCart(AddCartRequest request) {
        Long saleId = null;
        if (!StringUtils.isEmpty(request.getPhoneNumber())) {
            EndUser endUserByPhoneNumber = endUserRepository.findEndUserByPhoneNumber(request.getPhoneNumber());
            if (endUserByPhoneNumber != null) {
                saleId = request.getCustomerId();
                request.setCustomerId(endUserByPhoneNumber.getAccountId());
            }
        }
        Cart byCustomerIdAndAndJewelryId = cartRepository.findByUserIdAndAndJewelryId(request.getCustomerId(), request.getJewelryId());
        if (byCustomerIdAndAndJewelryId != null) {
            //update
            byCustomerIdAndAndJewelryId.setQuantity(byCustomerIdAndAndJewelryId.getQuantity() + request.getQuantity());
            byCustomerIdAndAndJewelryId.setUpdatedAt(new java.util.Date());
            cartRepository.updateByUserIdAndJewelryId(request.getCustomerId(), request.getJewelryId(), byCustomerIdAndAndJewelryId.getQuantity(), byCustomerIdAndAndJewelryId.getUpdatedAt(), byCustomerIdAndAndJewelryId.getStatus(), request.getSize());
        } else {
            //insert
            Cart cart = new Cart();
            cart.setJewelryId(request.getJewelryId());
            cart.setQuantity(request.getQuantity());
            cart.setUserId(request.getCustomerId());
            cart.setCreatedAt(new Date(new java.util.Date().getTime()));
            cart.setStatus(ACTIVE_CART);
            cart.setSize(request.getSize());
            cart.setSaleId(saleId);
            cartRepository.save(cart);
        }

        return true;
    }


    @Override
    public Boolean updateCart(AddCartRequest request) {
        if (!StringUtils.isEmpty(request.getPhoneNumber())) {
            EndUser endUserByPhoneNumber = endUserRepository.findEndUserByPhoneNumber(request.getPhoneNumber());
            if (endUserByPhoneNumber != null) {
                request.setCustomerId(endUserByPhoneNumber.getAccountId());
            }
        }
        Cart byCustomerIdAndAndJewelryId = cartRepository.findByUserIdAndAndJewelryId(request.getCustomerId(), request.getJewelryId());
        if (byCustomerIdAndAndJewelryId != null) {
            //update
            byCustomerIdAndAndJewelryId.setQuantity(byCustomerIdAndAndJewelryId.getQuantity() + request.getQuantity());
            byCustomerIdAndAndJewelryId.setUpdatedAt(new java.util.Date());
            byCustomerIdAndAndJewelryId.setSize(request.getSize());
            if (!StringUtils.isEmpty(request.getStatus())) {
                byCustomerIdAndAndJewelryId.setStatus(request.getStatus());
            }
            cartRepository.updateByUserIdAndJewelryId(request.getCustomerId(), request.getJewelryId(), byCustomerIdAndAndJewelryId.getQuantity(), byCustomerIdAndAndJewelryId.getUpdatedAt(), byCustomerIdAndAndJewelryId.getStatus(), request.getSize());
        }
        return true;
    }

    @Override
    public Boolean deleteCart(DeleteCartRequest request) {
        cartRepository.deleteById(request.getCartId());
        return true;
    }

    @Override
    public Object detail(GetOrderDetailRequest request) {
        Orders order = ordersRepository.findById(request.getOrderId()).orElseThrow(() -> new DiamondShopException(400, "Order not found"));

        List<OrdersListAllUser> ordersListAllUsers = new ArrayList<>();
        OrdersListAllUser ordersListAllUser = new OrdersListAllUser();
        BeanUtils.copyProperties(order, ordersListAllUser);
        List<OrderDetail> allByUniqueOrderId = orderDetailRepository.findAllByUniqueOrderId(order.getUniqueOrderId());
        ordersListAllUser.setOrderDetails(allByUniqueOrderId);
        ordersListAllUser.setDeliveryInfo(deliveryRepository.findAllByOrderId(order.getUniqueOrderId()));
        //ordersListAllUser.setPhoneNumber(request.getPhoneNumber());
        ordersListAllUsers.add(ordersListAllUser);

        Meta meta = new Meta(request.getRequestId(), 200, "success", HttpStatus.OK.toString());
        BaseResponse response = new BaseResponse(meta, ordersListAllUsers);

        return ordersListAllUsers;
    }

    @Override
    public ResponseEntity<Object> orderListAllUser(GetListOrderRequest request) {
        if (request.getLimit() == null) {
            request.setLimit(10);
        }
        if (request.getOffset() == null) {
            request.setOffset(0);
        }
        Page<OrdersListAllUser> ordersListAllUsers2 = ordersRepository.searchAllOrders(request.getStatus(), request.getOrderId(), request.getPhoneNumber(), PageRequest.of(request.getOffset() / request.getLimit(), request.getLimit(),Sort.by(Sort.Direction.DESC, "id")));
//        List<OrdersListAllUser> ordersListAllUsers = ordersRepository.searchAllOrders(request.getStatus(), request.getOrderId(), request.getPhoneNumber());
        for (OrdersListAllUser order : ordersListAllUsers2.getContent()) {
            List<OrderDetail> allByUniqueOrderId = orderDetailRepository.findAllByUniqueOrderId(order.getUniqueOrderId());
            order.setOrderDetails(allByUniqueOrderId);
            order.setDeliveryInfo(deliveryRepository.findAllByOrderId(order.getUniqueOrderId()));
//            ordersListAllUsers.add(order);
//        }
//        for(OrdersListAllUser ordersListAllUser : ordersListAllUsers){
            Optional<EndUser> endUserByAccountId = endUserRepository.findEndUserByAccountId(order.getCustomerId());
            EndUser endUser = endUserByAccountId.get();
            order.setPhoneNumber(endUser != null ? endUser.getPhoneNumber() : null);
        }
        Meta meta = new Meta(request.getRequestId(), 200, "success", HttpStatus.OK.toString());
        meta.setLimit(request.getLimit());
        meta.setOffset(request.getOffset());
        meta.setTotal((int) ordersListAllUsers2.getTotalElements()); //(int) ordersListAllUsers.getTotalElements()
        BaseResponse response = new BaseResponse(meta, ordersListAllUsers2.getContent());

        return ResponseEntity.ok(response);
    }

    @Override
    public Boolean updateOrder(UpdateOrderRequest request) {
        if (!StringUtils.isEmpty(request.getPhoneNumber())) {
            EndUser endUserByPhoneNumber = endUserRepository.findEndUserByPhoneNumber(request.getPhoneNumber());
            request.setCustomerId(endUserByPhoneNumber.getAccountId());
        }
        Orders ordersRepositoryById = ordersRepository.findByUniqueOrderId(request.getOrderId()).get();
        if (ordersRepositoryById != null) {
            //update order list
            if (Objects.nonNull(request.getStatusOrder())) {
                ordersRepositoryById.setStatus(request.getStatusOrder());
                ordersRepository.save(ordersRepositoryById);
                //order detail
                List<OrderDetail> allByUniqueOrderId = orderDetailRepository.findAllByUniqueOrderId(ordersRepositoryById.getUniqueOrderId());
                for (OrderDetail orderDetail : allByUniqueOrderId) {
                    orderDetail.setStatus(request.getStatusOrder());
                    orderDetailRepository.save(orderDetail);
                }
            }
            if(StringUtils.isEmpty(request.getStatusDelivery())){
                if(StatusOrder.DONE.equals(request.getStatusOrder())){
                    request.setStatusDelivery(StatusDelivery.SUCCESS_DELIVERY.getValue());
                }else if(StatusOrder.CANCEL.equals(request.getStatusOrder())){
                    request.setStatusDelivery(StatusDelivery.FAIL_DELIVERY.getValue());
                }
            }

            //update delivery
            if (Objects.nonNull(request.getStatusDelivery())) {
                Delivery delivery = deliveryRepository.findAllByOrderId(ordersRepositoryById.getUniqueOrderId());
                delivery.setStatus(request.getStatusDelivery());
                delivery.setUpdatedAt(new Date());
                deliveryRepository.save(delivery);
                //update deliver
                Deliver deliverRepositoryByUserId = deliverRepository.findById(delivery.getDeliverId()).get();
                deliverRepositoryByUserId.setTotalOrder(deliverRepositoryByUserId.getTotalOrder() + 1);
                if (StatusDelivery.SUCCESS_DELIVERY.getValue().equals(request.getStatusDelivery())) {
                    deliverRepositoryByUserId.setTotalOrderSuccess(deliverRepositoryByUserId.getTotalOrderSuccess() + 1);
                } else {
                    deliverRepositoryByUserId.setTotalOrderFail(deliverRepositoryByUserId.getTotalOrderFail() + 1);
                }
                deliverRepository.save(deliverRepositoryByUserId);
            }
        }
        return true;
    }

    @Override
    public Object preorderDetail(UserPrincipal userPrincipal) {
        PreOrderDetailResponse preOrderDetailResponse = new PreOrderDetailResponse();
        preOrderDetailResponse.setUserProfile(accountService.profile(userPrincipal.getId()));
        return preOrderDetailResponse;
    }

    @Override
    public DashboardResponse dashboard(Date fromDate, Date toDate) {
        DashboardResponse dashboardResponse = new DashboardResponse();

        DashboardResponse.OrdersData ordersData = new DashboardResponse.OrdersData();
        ordersData.setTotalOrder(ordersRepository.findAllOrderByDate(fromDate, toDate));
        ordersData.setOrderInit(ordersRepository.findAllOrderByStatusAndDate(StatusOrder.INIT.getValue(), fromDate, toDate));
        ordersData.setOrderWaitPayment(ordersRepository.findAllOrderByStatusAndDate(StatusOrder.CREATE_PAYMENT.getValue(), fromDate, toDate));
        ordersData.setOrderDelivery(ordersRepository.findAllOrderByStatusAndDate(StatusOrder.DELIVERY.getValue(), fromDate, toDate));
        ordersData.setOrderSuccess(ordersRepository.findAllOrderByStatusAndDate(StatusOrder.DONE.getValue(), fromDate, toDate));
        ordersData.setOrderCancel(ordersRepository.findAllOrderByStatusAndDate(StatusOrder.CANCEL.getValue(), fromDate, toDate));

        DashboardResponse.RevenueData revenueData = new DashboardResponse.RevenueData();
        revenueData.setPriceInit(ordersRepository.getTotalStatusAmountAndDate(StatusOrder.INIT.getValue(),fromDate, toDate));
        revenueData.setPriceWaitPayment(ordersRepository.getTotalStatusAmountAndDate(StatusOrder.CREATE_PAYMENT.getValue(),fromDate, toDate));
        revenueData.setPriceDelivery(ordersRepository.getTotalStatusAmountAndDate(StatusOrder.DELIVERY.getValue(),fromDate, toDate));
        revenueData.setPriceSuccess(ordersRepository.getTotalStatusAmountAndDate(StatusOrder.DONE.getValue(),fromDate, toDate));
        revenueData.setPriceCancel(ordersRepository.getTotalStatusAmountAndDate(StatusOrder.CANCEL.getValue(),fromDate, toDate));
        revenueData.setTotalPrice(ordersRepository.getTotalStatusAmountAndDate(null, null, null));

        dashboardResponse.setRevenueData(revenueData);
        dashboardResponse.setOrderInfo(ordersData);
        return dashboardResponse;
    }

    @Override
    public void sendMailInvoice(SendInvoiceRequest request) {
    }

    @Override
    public Object getInvoice(String orderId) {
        Invoice invoice = invoiceRepository.findByOrderId(orderId);
        return invoice.getHtmlContent();
    }

    @Override
    public ResponseEntity<Object> saleList(GetListSaleRequest request) {
        List<DashboardResponse.SaleData> saleDataList = new ArrayList<>();
        List<Long> listSaleId = orderDetailRepository.getListSaleId(request.getCustomerId(), request.getStatus());
        for (Long saleId : listSaleId) {
            if(saleId != null){
                saleDataList.add(processSaleData(saleId));
            }
        }
        Meta meta = new Meta(request.getRequestId(), 200, "success", HttpStatus.OK.toString());
        BaseResponse response = new BaseResponse(meta, saleDataList);

        return ResponseEntity.ok(response);
    }

    @Override
    public DashboardResponse.SaleData saleDetail(Long saleId) {
        return processSaleData(saleId);
    }

    @Override
    public Object invoiceDetal(GetListOrderRequest request) {
        InvoiceDetailResponse invoiceDetailResponse = new InvoiceDetailResponse();
        invoiceDetailResponse.setOrderId(request.getOrderId());
        Orders orders = ordersRepository.findByUniqueOrderId(request.getOrderId()).get();
        invoiceDetailResponse.setOrderDate(orders.getCreatedAt());
        EndUser endUserByAccountId = endUserRepository.findEndUserByAccountId(orders.getCustomerId()).get();
        invoiceDetailResponse.setPhoneNumber(endUserByAccountId.getPhoneNumber());
        invoiceDetailResponse.setCustomerName(endUserByAccountId.getFullName());
        List<OrderDetail> allByUniqueOrderId = orderDetailRepository.findAllByUniqueOrderId(request.getOrderId());
        List<InvoiceDetailResponse.Product> productList = new ArrayList<>();
        if(allByUniqueOrderId != null){
            for (OrderDetail orderDetail : allByUniqueOrderId) {
                InvoiceDetailResponse.Product product = new InvoiceDetailResponse.Product();
                product.setQuantity(orderDetail.getQuantityNumber());
                Jewelry jewelryById = jewelryRepository.findJewelryById(orderDetail.getJewelryId());
                product.setJewelryName(jewelryById.getName());
                product.setJewelryCode(jewelryById.getJewelryCode());
                productList.add(product);
            }
        }
        invoiceDetailResponse.setListOfProduct(productList);
        return invoiceDetailResponse;
    }

    @Autowired
    private ShapeRepository shapeRepository;

    @Autowired
    private CutRepository cutRepository;

    @Autowired
    private ClarityRepository clarityRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Override
    public Object giaInfo(GetListOrderRequest request) {
        List<GIAInfoResponse> giaInfoResponseList = new ArrayList<>();
        GIAInfoResponse giaInfoResponse = new GIAInfoResponse();
        List<OrderDetail> allByUniqueOrderId = orderDetailRepository.findAllByUniqueOrderId(request.getOrderId());
        if(allByUniqueOrderId != null){
            for (OrderDetail orderDetail : allByUniqueOrderId) {
                Diamond diamond = diamondRepository.findAllByName(orderDetail.getSize());
                giaInfoResponse.setDiamond(diamond);
                giaInfoResponse.setInvoiceId(orderDetail.getId());
                giaInfoResponse.setShapeCut(shapeRepository.findById(diamond.getShapeId()).get().getShape());
                giaInfoResponse.setCut(cutRepository.findById(diamond.getCutId()).get().getCut());
                giaInfoResponse.setNameDiamond(diamond.getName());
                giaInfoResponse.setClarity(clarityRepository.findById(diamond.getClarityId()).get().getClarity());
                giaInfoResponse.setColor(colorRepository.findById(diamond.getColorId()).get().getColor());
                giaInfoResponse.setCarat(diamond.getCarat());
                giaInfoResponseList.add(giaInfoResponse);
            }
        }

        return giaInfoResponseList;
    }

    private DashboardResponse.SaleData processSaleData(Long saleId){
    DashboardResponse.SaleData saleData = new DashboardResponse.SaleData();
    saleData.setTotalPrice(orderDetailRepository.getSaleInfo(saleId, null));
    saleData.setTotalOrder(orderDetailRepository.getOrderIdBySaleId(saleId, null).size());
    saleData.setSaleId(saleId);
    saleData.setTotalOrderSuccess(orderDetailRepository.getOrderIdBySaleId(saleId, StatusOrder.DONE.getValue()).size());
    saleData.setTotalPriceSuccess(orderDetailRepository.getSaleInfo(saleId, StatusOrder.DONE.getValue()));
    return saleData;
}
    private void addInvoice(SendInvoiceRequest request) {
        Map<String, Object> attribute = new HashMap<>();
        attribute.put("orderId", request.getOrderId());
        attribute.put("date", request.getDate());
        attribute.put("mail", request.getMail());
        attribute.put("customerId", request.getCustomerId());
        attribute.put("total", request.getTotal());
        attribute.put("address", request.getAddress());
        attribute.put("products", request.getProducts());

        String invoiceHtml = mailService.getHtmlContent(attribute, "invoice_mail_template.ftl");
        invoiceRepository.save(new Invoice(request.getOrderId(), invoiceHtml));
    }
}
