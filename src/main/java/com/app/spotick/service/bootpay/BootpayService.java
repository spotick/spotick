package com.app.spotick.service.bootpay;

import com.app.spotick.api.response.BootPayResponse;
import com.app.spotick.domain.entity.place.PlacePayment;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.ticket.TicketOrder;
import com.app.spotick.domain.type.payment.PaymentStatus;
import com.app.spotick.domain.type.payment.PaymentType;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.repository.place.payment.PlacePaymentRepository;
import com.app.spotick.repository.ticket.order.TicketOrderRepository;
import kr.co.bootpay.Bootpay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BootpayService {

    @Value("${bootpay.restApi}")
    private String REST_API;
    @Value("${bootpay.privateKey}")
    private String PRIVATE_KEY;
    private Bootpay bootpay;
    private final PlacePaymentRepository placePaymentRepository;
    private final TicketOrderRepository ticketOrderRepository;

    // 부트페이와 연결을 하기위한 토큰 발급 코드
    private void getBootpayToken() {
        try {
            bootpay = new Bootpay(REST_API, PRIVATE_KEY);
            HashMap token = bootpay.getAccessToken();
            if (token.get("error_code") != null) {
                System.out.println("getAccessToken false: " + token);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // bootpay에서 발생된 receiptId를 통해 영수증을 조회하는 메소드
    public HashMap<String, Object> getBootpayReceipt(String receiptId) {
        try {
            getBootpayToken();

            return bootpay.getReceipt(receiptId);
        } catch (Exception e) {
            log.error("부트페이 영수증 확인 실패, [Err_Msg]: {}", e.getMessage());
            return null;
        }

    }

    // 결제를 승인하기 위한 메소드
    private HashMap<String, Object> confirm(String receiptId, PaymentType paymentType) {
        try {
            getBootpayToken();
            HashMap<String, Object> res = bootpay.confirm(receiptId);

            // place
            if (paymentType == PaymentType.PLACE) {
                if (res.get("error_code") == null) { //success
                    System.out.println("confirm success: " + res);

                    // order테이블의 status column 데이터를 바꿔준다.
                    Long orderId = Long.valueOf(res.get("order_id").toString());
                    PlacePayment foundPayment = placePaymentRepository.findById(orderId).orElseThrow(
                            NoSuchElementException::new
                    );
                    foundPayment.updatePaymentStatus(PaymentStatus.APPROVED);

                    PlaceReservation foundReservation = placePaymentRepository.findReservationByPaymentId(Long.valueOf(res.get("order_id").toString()));
                    foundReservation.updateStatus(PlaceReservationStatus.APPROVED);
                } else {
                    System.out.println("confirm false: " + res);
                }

                // ticket
            } else if (paymentType == PaymentType.TICKET) {
                if (res.get("error_code") == null) { //success
                    System.out.println("confirm success: " + res);

                    // order테이블의 status column 데이터를 바꿔준다.
                    Long orderId = Long.valueOf(res.get("order_id").toString());
                    TicketOrder foundOrder = ticketOrderRepository.findById(orderId).orElseThrow(
                            NoSuchElementException::new
                    );
                    foundOrder.updatePaymentStatus(PaymentStatus.APPROVED);
                } else {
                    System.out.println("confirm false: " + res);
                }
            }

            return res;
        } catch (Exception e) {
            log.error("결제 확인 오류 [Err_Msg]: {}", e.getMessage());
            return null;
        }
    }

    private void deny(String orderId, PaymentType paymentType) {
        if (paymentType == PaymentType.PLACE) {
            PlacePayment foundPayment = placePaymentRepository.findById(Long.valueOf(orderId)).orElseThrow(
                    NoSuchElementException::new
            );
            foundPayment.updatePaymentStatus(PaymentStatus.DECLINED);
        }
    }

    // 1. 부트페이로 전달받은 영수증의 결제 가격과 TBL_PLACE_PAYMENT의 AMOUNT필드를 조회하여 결제 가격을 체크한다.
    // 2. 일치시 confirm() 실행, 아닐시 deny()실행
    public ResponseEntity<?> priceCheck(String orderId, PaymentType paymentType) {
        // 부트페이쪽에서 받은 영수증을 확인.
        HashMap<String, Object> res = getBootpayReceipt(orderId);

        System.out.println("res = " + res);

        long receiptPrice = Long.parseLong(res.get("price").toString());

        System.out.println("receiptPrice = " + receiptPrice);

        // PLACE와 TICKET타입 구분
        if (paymentType == PaymentType.PLACE) {
            // orderId를 통하여 DB에 저장된 총 가격을 가져온다. 없을 시 Exception
            Long fullPrice = placePaymentRepository.findAmountById(Long.valueOf(res.get("order_id").toString())).orElseThrow(
                    NoSuchElementException::new
            );

            System.out.println("fullPrice = " + fullPrice);

            // 부트페이와 DB에 저장된 가격을 일치하는지 확인 한다.
            // 일치할 시: confirm() 실행 후 결제 성공을 return한다.
            // 일치하지 않을 시
            if (receiptPrice == fullPrice) {
                HashMap<String, Object> confirmationData = confirm(orderId, PaymentType.PLACE);
                System.out.println("confirmationData = " + confirmationData);

                return new ResponseEntity<>(
                        BootPayResponse.builder()
                                .success(true)
                                .code(0)
                                .message("결제 성공")
                                .data(confirmationData)
                                .build(),
                        HttpStatus.OK
                );
            } else {
                deny(orderId, paymentType);

                return new ResponseEntity<>(
                        BootPayResponse.builder()
                                .success(false)
                                .code(2)
                                .message("결제가 거부되었습니다.")
                                .build(),
                        HttpStatus.FORBIDDEN
                );
            }
        } else if (paymentType == PaymentType.TICKET) {
            // orderId를 통하여 DB에 저장된 총 가격을 가져온다. 없을 시 Exception
            Long fullPrice = ticketOrderRepository.findAmountById(Long.valueOf(res.get("order_id").toString())).orElseThrow(
                    NoSuchElementException::new
            );

            System.out.println("fullPrice = " + fullPrice);

            // 부트페이와 DB에 저장된 가격을 일치하는지 확인 한다.
            // 일치할 시: confirm() 실행 후 결제 성공을 return한다.
            // 일치하지 않을 시
            if (receiptPrice == fullPrice) {
                HashMap<String, Object> confirmationData = confirm(orderId, PaymentType.TICKET);
                System.out.println("confirmationData = " + confirmationData);

                return new ResponseEntity<>(
                        BootPayResponse.builder()
                                .success(true)
                                .code(0)
                                .message("결제 성공")
                                .data(confirmationData)
                                .build(),
                        HttpStatus.OK
                );
            } else {
                deny(orderId, paymentType);

                return new ResponseEntity<>(
                        BootPayResponse.builder()
                                .success(false)
                                .code(2)
                                .message("결제가 거부되었습니다.")
                                .build(),
                        HttpStatus.FORBIDDEN
                );
            }
        }
        return null;
    }
}
