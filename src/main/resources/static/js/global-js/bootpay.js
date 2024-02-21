const payService = (function () {
    const PAY_METHOD = "DEBIT_CARD";

    function requestPlacePaymentSave(reservationId, callback) {
        const dto = {
            save: {
                reservationId: reservationId,
                paymentMethod: PAY_METHOD,
            }
        };

        fetch('/order/api/place/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dto),
        })
            .then(response => {
                if (!response.ok) {
                    throw response;
                }
                return response.json();
            })
            .then(data => {
                console.log('Success:', data);

                if (callback) {
                    callback(data.data);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                // 에러 처리 로직
            });
    }

    async function payItem(data) {
        console.log(data)
        try {
            const response = await Bootpay.requestPayment({
                "application_id": "59a4d323396fa607cbe75de4",
                "price": 100,
                "order_name": "테스트결제",
                "order_id": data,
                "pg": "다날",
                "method": "카드",
                "tax_free": 0,
                "user": {
                    "id": "회원아이디",
                    "username": "회원이름",
                    "phone": "01000000000",
                    "email": "test@test.com"
                },
                "items": [
                    {
                        "id": "item_id",
                        "name": "테스트아이템",
                        "qty": 1,
                        "price": 100
                    }
                ],
                "extra": {
                    "open_type": "iframe",
                    "card_quota": "0,2,3",
                    "escrow": false,
                    "separately_confirmed": true,
                }
            })
            switch (response.event) {
                case 'issued':
                    // 가상계좌 입금 완료 처리
                    break
                case 'done':
                    console.log(response)
                    // 결제 완료 처리
                    break
                case 'confirm': //payload.extra.separately_confirmed = true; 일 경우 승인 전 해당 이벤트가 호출됨
                    fetch(`/bootpay/api/place/check?orderId=${response.receipt_id}`, {
                        method: "POST",
                    })
                        .then((res) => res.json())
                        .then((result) => {
                            // 정상적으로 처리되었는지 메시지를 띄운다.
                            // 정상적으로 승인이 되면 코드에 0을 반환할 것이고,
                            if (result.code === 0) {
                                // 결제창을 닫는다.
                                Bootpay.destroy();
                                alert(result.message);

                                // 아니라면 다른 숫자가 반환된다.
                            } else {
                                alert(result.message);
                            }
                            location.replace("/");
                        })
                        .catch((err) => {
                            console.error(err);
                        });
                    break;
                    if (confirmedData.event === 'done') {
                        //결제 성공
                    }

                    /**
                     * 2. 서버 승인을 하고자 할때
                     * // requestServerConfirm(); //예시) 서버 승인을 할 수 있도록  API를 호출한다. 서버에서는 재고확인과 로직 검증 후 서버승인을 요청한다.
                     * Bootpay.destroy(); //결제창을 닫는다.
                     */
                    break
            }
        } catch (e) {
            // 결제 진행중 오류 발생
            // e.error_code - 부트페이 오류 코드
            // e.pg_error_code - PG 오류 코드
            // e.message - 오류 내용
            console.log(e.message)
            switch (e.event) {
                case 'cancel':
                    // 사용자가 결제창을 닫을때 호출
                    console.log(e.message);
                    break
                case 'error':
                    // 결제 승인 중 오류 발생시 호출
                    console.log(e.error_code);
                    break
            }
        }
    }

    return {
        requestPlacePaymentSave: requestPlacePaymentSave,
        payItem: payItem
    }
})();