package vn.fpt.diamond_shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.diamond_shop.config.ApplicationContextHolder;
import vn.fpt.diamond_shop.jobs.RenewImageJob;
import vn.fpt.diamond_shop.request.ManagerModifyAccountRequest;
import vn.fpt.diamond_shop.service.AdminService;
import vn.fpt.diamond_shop.service.Impl.AdminServiceImpl;
import vn.fpt.diamond_shop.util.logger.LogActivities;

@Slf4j
@RestController
@RequestMapping("/shop/admin")
public class AdminController extends BaseController {

    @GetMapping("/account")
    @LogActivities
    private ResponseEntity<?> listAccount() {
        return ok(ApplicationContextHolder.getContext().getBean(AdminServiceImpl.class).listAccount());
    }

    @PutMapping("/account/modify")
    @LogActivities
    private ResponseEntity<?> modify(@RequestBody ManagerModifyAccountRequest request) {
        ApplicationContextHolder.getContext().getBean(AdminServiceImpl.class).changeInforAccount(request);
        return ok("Change account success");
    }

    @PutMapping("/job/renew-image")
    @LogActivities
    private ResponseEntity<?> renewImage() {
        ApplicationContextHolder.getContext().getBean(RenewImageJob.class).renewImageTask();
        return noContent();
    }

    @PostMapping("/job/check-send-mail-coupon")
    @LogActivities
    private ResponseEntity<?> checkSendMailCoupon() {
        ApplicationContextHolder.getContext().getBean(AdminServiceImpl.class).checkSendMailCoupon();
        return noContent();
    }
}
