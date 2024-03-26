package com.app.spotick.api.controller.order;

import com.app.spotick.domain.type.payment.PaymentStatus;
import com.app.spotick.domain.type.payment.PaymentType;
import com.app.spotick.service.bootpay.BootpayService;
import com.app.spotick.service.place.payment.PlacePaymentService;
import com.app.spotick.service.ticket.order.TicketOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/bootpay/api")
@RequiredArgsConstructor
public class BootpayRestController {
    private final BootpayService bootpayService;
    private final PlacePaymentService placePaymentService;
    private final TicketOrderService ticketOrderService;

    @GetMapping("/place/check")
    public ResponseEntity<?> placeCheck(@RequestParam("receiptId") String receiptId) {
        return bootpayService.priceCheck(receiptId, PaymentType.PLACE);
    }

    @PatchMapping("/place/reject")
    public ResponseEntity<String> rejectPlacePayment(@RequestParam("paymentId") Long paymentId) {
        try {
            placePaymentService.updateStatus(paymentId, PaymentStatus.DECLINED);

            return ResponseEntity.ok("거부 처리 완료");
        } catch (Exception e) {
            log.error("[Err_Msg]: {}", e.getMessage());
            return ResponseEntity.badRequest().body("오류 발생");
        }
    }

    @GetMapping("/ticket/check")
    public ResponseEntity<?> ticketCheck(@RequestParam("receiptId") String receiptId) {
        return bootpayService.priceCheck(receiptId, PaymentType.TICKET);
    }

    @PatchMapping("/ticket/reject")
    public ResponseEntity<String> rejectTicketPayment(@RequestParam("orderId") Long orderId) {
        try {
            ticketOrderService.declineOrder(orderId, PaymentStatus.DECLINED);

            return ResponseEntity.ok("거부 처리 완료");
        } catch (Exception e) {
            log.error("[Err_Msg]: {}", e.getMessage());
            return ResponseEntity.badRequest().body("오류 발생");
        }
    }
}
