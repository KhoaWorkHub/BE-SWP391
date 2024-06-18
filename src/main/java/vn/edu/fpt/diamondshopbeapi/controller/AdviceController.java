package vn.edu.fpt.diamondshopbeapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.edu.fpt.diamondshopbeapi.payload.BaseResponse;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {

        return ResponseEntity.status(500).body(new BaseResponse(500, false, e.getMessage()));
    }
}
