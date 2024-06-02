package vn.fpt.diamond_shop.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.fpt.diamond_shop.controller.BaseController;
import vn.fpt.diamond_shop.security.exception.BadRequestException;
import vn.fpt.diamond_shop.security.exception.ResourceNotFoundException;

@ControllerAdvice
public class AppControllerAdvice extends BaseController {

    @ExceptionHandler(value = {
            BadRequestException.class,
            DiamondShopException.class,
            ResourceNotFoundException.class
    })
    protected ResponseEntity<?> badReq(RuntimeException e) {
        return err(e.getMessage(), null);
    }

    @ExceptionHandler(value = {
            Exception.class
    })
    protected ResponseEntity<?> internalErr(Exception e) {
        return internalErr(e.getMessage(), e, null);
    }
}
