package vn.fpt.diamond_shop.security.endpoint;

import org.springframework.web.bind.annotation.*;
import vn.fpt.diamond_shop.controller.BaseController;
import vn.fpt.diamond_shop.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/shop/user")
public class UserController extends BaseController {
    @Autowired
    private UserRepository userRepository;
}
