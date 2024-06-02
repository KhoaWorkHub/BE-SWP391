package vn.fpt.diamond_shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fpt.diamond_shop.request.ManagerModifyAccountRequest;
import vn.fpt.diamond_shop.service.AdminService;

@RestController
@RequestMapping("/shop/admin")
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/account")
    private ResponseEntity<?> listAccount() {
        return ok(adminService.listAccount());
    }

    @PutMapping("/account/modify")
    private ResponseEntity<?> modify(@RequestBody ManagerModifyAccountRequest request) {
        adminService.changeInforAccount(request);
        return ok("Change account success");
    }
}
