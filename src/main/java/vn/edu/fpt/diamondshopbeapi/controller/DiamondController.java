package vn.edu.fpt.diamondshopbeapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.diamondshopbeapi.payload.BaseResponse;
import vn.edu.fpt.diamondshopbeapi.service.IDiamondService;

@RestController
@RequestMapping("/shop/diamond")
public class DiamondController {

    @Autowired
    private IDiamondService diamondService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDiamonds() {

        return ResponseEntity.ok(new BaseResponse("ok"));
    }

    @GetMapping("/exception")
    public ResponseEntity<?> exception() {
        throw new RuntimeException("Bug");
    }
}
