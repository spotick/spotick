package com.app.spotick.api.controller.order;

import com.app.spotick.service.bootpay.BootpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bootpay/api")
@RequiredArgsConstructor
public class BootpayRestController {
    private final BootpayService bootpayService;

    @PostMapping("/place/check")
    public ResponseEntity<?> priceCheck(@RequestParam("orderId") String orderId) {
        return bootpayService.priceCheck(orderId);
    }
}
