package com.app.spotick.service.bootpay;

import com.app.spotick.domain.entity.place.PlacePayment;
import com.app.spotick.domain.type.payment.PaymentStatus;
import com.app.spotick.repository.place.payment.PlacePaymentRepository;
import kr.co.bootpay.Bootpay;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BootpayService {
    private Bootpay bootpay;
    private final PlacePaymentRepository placePaymentRepository;


    private void getBootpayToken() {
        try {
            bootpay = new Bootpay("59a4d32b396fa607c2e75e00", "t3UENPWvsUort5WG0BFVk2+yBzmlt3UDvhDH2Uwp0oA=");
            HashMap token = bootpay.getAccessToken();
            if (token.get("error_code") != null) {
                System.out.println("getAccessToken false: " + token);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap getBootpayReceipt(String receiptId) {
        try {
            getBootpayToken();
            HashMap res = bootpay.getReceipt(receiptId);
            if (res.get("error_code") == null) { // success
                System.out.println("getReceipt success: " + res);
            } else {
                System.out.println("getReceipt false: " + res);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public HashMap confirm(String receiptId){
        try {
            getBootpayToken();
            HashMap res = bootpay.confirm(receiptId);
            if(res.get("error_code") == null) { //success
                System.out.println("confirm success: " + res);

                // order테이블의 status column 데이터를 바꿔준다.
                Long orderId = Long.valueOf(res.get("order_id").toString());
                PlacePayment placePayment = placePaymentRepository.findById(orderId)
                        .orElseThrow(() -> {
                            System.out.println("주문 번호에 해당하는 주문 정보가 없음.");
                            return new NoSuchElementException();
                        });

                placePayment.updatePaymentStatus(PaymentStatus.APPROVED);
            } else {
                System.out.println("confirm false: " + res);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> priceCheck(String orderId) {
        // 여기 메인 로직 구현 예정
        getBootpayReceipt(orderId);
        confirm(orderId);
        return null;
    }
}
