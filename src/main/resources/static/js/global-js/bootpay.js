export const payService = (function () {
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

    function requestTicketPaymentSave(ticketId, eventDate, ticketOrderDetailDtoList, callback) {
        const dto = {
            ticketId: ticketId,
            eventDate: eventDate,
            paymentMethod: PAY_METHOD,
            ticketOrderDetailDtoList: ticketOrderDetailDtoList
        };

        fetch('/order/api/ticket/save', {
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

                console.log(callback)
                if (callback) {
                    console.log('실행은 되는거임?');
                    callback(data.data);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                // 에러 처리 로직
            });
    }

    async function payItem(data) {
        try {
            const response = await Bootpay.requestPayment({
                "application_id": "653a391400be04001c8e27f6",
                "price": data.amount,
                "order_name": data.placeTitle,
                "order_id": data.placePaymentId,
                "pg": "나이스페이",
                "method": "카드",
                "tax_free": 0,
                "user": {
                    "id": data.userId,
                    "username": data.nickname,
                    "phone": data.tel,
                    "email": data.email
                },
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
                    fetch(`/bootpay/api/place/check?receiptId=${response.receipt_id}`, {
                        method: "POST",
                    })
                        .then((res) => res.json())
                        .then((result) => {
                            console.log(result)

                            // ResponseEntity의 결과 값중 코드는 부트페이의 status항목과 일치한다.
                            // 이 항목을 이용하여 결과를 컨트롤한다.
                            if (result.code === 0) {
                                // 결제창을 닫는다.
                                Bootpay.destroy();
                                alert(result.message);

                                // 아니라면 다른 숫자가 반환된다.
                            } else {
                                console.log(result.message);
                                alert("결제 도중 오류가 발생했습니다. 다시 시도해주세요.");
                            }
                            location.replace("/mypage/reservations");
                        })
                        .catch((err) => {
                            console.error(err);
                        });
                    break;
                    if (confirmedData.event === 'done') {
                        //결제 성공
                    }


                    break
            }
        } catch (e) {
            console.log(e.message)
            switch (e.event) {
                case 'cancel':
                    // 사용자가 결제창을 닫을때 호출
                    console.log(e.message);

                    fetch(`/bootpay/api/place/reject?paymentId=${data.placePaymentId}`, {
                        method: 'PUT'
                    })
                        .then(response => {
                            if (response.ok) {
                                alert("결제가 취소되었습니다.");
                            } else {
                                alert("거부 처리에 실패했습니다. 잠시 후 다시 시도해주세요.");
                            }
                        })
                        .catch(error => {
                            console.error("오류 발생:", error);
                            alert("오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                        });
                    break;
                case 'error':
                    // 결제 승인 중 오류 발생시 호출
                    console.log(e.error_code);

                    fetch(`/bootpay/api/place/reject?paymentId=${data.placePaymentId}`, {
                        method: 'PUT'
                    })
                        .then(response => {
                            if (response.ok) {
                                alert("결제 오류가 발생되었습니다. 결제를 취소합니다.");
                            }
                        })
                        .catch(error => {
                            console.error("오류 발생:", error);
                            alert("오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                        });
                    break;
            }
        }
    }

    async function payTickets(data) {
        console.log('실행됨');
        console.log(data.bootpayItemDtoList)
        try {
            const response = await Bootpay.requestPayment({
                "application_id": "653a391400be04001c8e27f6",
                "price": data.amount,
                "order_name": data.ticketTitle,
                "order_id": data.orderId,
                "pg": "나이스페이",
                "method": "카드",
                "tax_free": 0,
                "user": {
                    "id": data.userId,
                    "username": data.nickname,
                    "phone": data.tel,
                    "email": data.email
                },
                "items": data.bootpayItemDtoList,
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
                    fetch(`/bootpay/api/place/check?receiptId=${response.receipt_id}`, {
                        method: "POST",
                    })
                        .then((res) => res.json())
                        .then((result) => {
                            console.log(result)

                            // ResponseEntity의 결과 값중 코드는 부트페이의 status항목과 일치한다.
                            // 이 항목을 이용하여 결과를 컨트롤한다.
                            if (result.code === 0) {
                                // 결제창을 닫는다.
                                Bootpay.destroy();
                                alert(result.message);

                                // 아니라면 다른 숫자가 반환된다.
                            } else {
                                console.log(result.message);
                                alert("결제 도중 오류가 발생했습니다. 다시 시도해주세요.");
                            }
                            location.replace("/mypage/reservations");
                        })
                        .catch((err) => {
                            console.error(err);
                        });
                    break;
                    if (confirmedData.event === 'done') {
                        //결제 성공
                    }


                    break
            }
        } catch (e) {
            console.log(e.message)
            switch (e.event) {
                case 'cancel':
                    // 사용자가 결제창을 닫을때 호출
                    console.log(e.message);

                    fetch(`/bootpay/api/place/reject?paymentId=${data.placePaymentId}`, {
                        method: 'PUT'
                    })
                        .then(response => {
                            if (response.ok) {
                                alert("결제가 취소되었습니다.");
                            } else {
                                alert("거부 처리에 실패했습니다. 잠시 후 다시 시도해주세요.");
                            }
                        })
                        .catch(error => {
                            console.error("오류 발생:", error);
                            alert("오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                        });
                    break;
                case 'error':
                    // 결제 승인 중 오류 발생시 호출
                    console.log(e.error_code);

                    fetch(`/bootpay/api/place/reject?paymentId=${data.placePaymentId}`, {
                        method: 'PUT'
                    })
                        .then(response => {
                            if (response.ok) {
                                alert("결제 오류가 발생되었습니다. 결제를 취소합니다.");
                            }
                        })
                        .catch(error => {
                            console.error("오류 발생:", error);
                            alert("오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                        });
                    break;
            }
        }
    }

    return {
        requestPlacePaymentSave: requestPlacePaymentSave,
        requestTicketPaymentSave: requestTicketPaymentSave,
        payItem: payItem,
        payTickets: payTickets,
    }
})();