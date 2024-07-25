package vn.fpt.diamond_shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.fpt.diamond_shop.constants.UrlConstants;
import vn.fpt.diamond_shop.request.*;
import vn.fpt.diamond_shop.security.CurrentUser;
import vn.fpt.diamond_shop.security.UserPrincipal;
import vn.fpt.diamond_shop.service.OrderService;
import vn.fpt.diamond_shop.util.logger.LogActivities;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(UrlConstants.BASIC_ORDER_URL)
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @PostMapping("list")
    @LogActivities
    public ResponseEntity<Object> list(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody GetListOrderRequest request) {
        request.setCustomerId(userPrincipal == null ? null : userPrincipal.getId());
        return orderService.orderList(request);
    }

    @PostMapping("add_order")
    @LogActivities
    public ResponseEntity<Object> addOrder(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody AddOrderRequest request) {
        request.setCustomerId(userPrincipal == null ? null : userPrincipal.getId());
        return ok(orderService.addOrder(request));
    }

    @PostMapping("detail")
    @LogActivities
    public ResponseEntity<Object> detail(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody GetOrderDetailRequest request) {
        request.setCustomerId(userPrincipal == null ? null : userPrincipal.getId());
        return ok(orderService.detail(request));
    }

    @PostMapping("update")
    @LogActivities
    public ResponseEntity<Object> update(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody UpdateOrderRequest request) {
        request.setCustomerId(userPrincipal == null ? null : userPrincipal.getId());
        return ok(orderService.updateOrder(request));
    }

    @PostMapping("list_all_user")
    @LogActivities
    public ResponseEntity<Object> listAllUser(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody GetListOrderRequest request) {
        request.setCustomerId(userPrincipal == null ? null : userPrincipal.getId());
        if(Boolean.TRUE == request.getIsDelivery()) {
            request.setDeliveryId(userPrincipal == null ? null : userPrincipal.getId());
        }
        return orderService.orderListAllUser(request);
    }

    @GetMapping("post-order")
    @LogActivities
    public ResponseEntity<Object> getInfoPostOrder(@CurrentUser UserPrincipal userPrincipal) {
        return ok(orderService.preorderDetail(userPrincipal));
    }

    @GetMapping("dashboard")
    @LogActivities
    public ResponseEntity<Object> getDashboard(@RequestParam(name = "fromDate", required = false) String fromDate,
                                               @RequestParam(name = "toDate", required = false) String toDate) throws Exception {
        Date from = StringUtils.isEmpty(fromDate) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
        Date to = StringUtils.isEmpty(toDate) ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDate + " 23:59:59");

//        request.setCustomerId(userPrincipal == null ? null : userPrincipal.getId());
        return ok(orderService.dashboard(from, to));
    }

    @PostMapping("sendMail")
    @LogActivities
    public ResponseEntity<Object> sendMail(@RequestBody SendInvoiceRequest request) {
        orderService.sendMailInvoice(request);
        return ok("Send mail success");
    }

    @GetMapping("get_invoice")
    @LogActivities
    public ResponseEntity<Object> getInvoice(@RequestParam String orderId) {
        return ok(orderService.getInvoice(orderId));
    }

    @PostMapping("sale_info")
    @LogActivities
    public ResponseEntity<Object> getSale(@Valid @RequestBody GetListSaleRequest request) {
        return orderService.saleList(request);
    }

    @PostMapping("sale_detail")
    @LogActivities
    public ResponseEntity<Object> getSaleDetail(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody GetListSaleRequest request) {
        request.setCustomerId(userPrincipal == null ? request.getSaleId() : userPrincipal.getId());
        return ok(orderService.saleDetail(request.getCustomerId()));
    }

    @PostMapping("invoice_detail")
    @LogActivities
    public ResponseEntity<Object> invoiceDetail(@Valid @RequestBody GetListOrderRequest request) {
        return ok(orderService.invoiceDetal(request));
    }

    @PostMapping("gia_info")
    @LogActivities
    public ResponseEntity<Object> giaInfo(@Valid @RequestBody GetListOrderRequest request) {
        return ok(orderService.giaInfo(request));
    }
}
