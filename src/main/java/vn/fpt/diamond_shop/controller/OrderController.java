package vn.fpt.diamond_shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.diamond_shop.constants.UrlConstants;
import vn.fpt.diamond_shop.request.AddOrderRequest;
import vn.fpt.diamond_shop.request.GetListCartRequest;
import vn.fpt.diamond_shop.request.GetListOrderRequest;
import vn.fpt.diamond_shop.service.OrderService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(UrlConstants.BASIC_ORDER_URL)
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @PostMapping("list")
    public ResponseEntity<Object> list(@Valid @RequestBody GetListCartRequest request) {
        return ok(orderService.listCart(request));
    }
    @PostMapping("add_order")
    public ResponseEntity<Object> addOrder(@Valid @RequestBody AddOrderRequest request) {
        return ok(orderService.addOrder(request));
    }
}
