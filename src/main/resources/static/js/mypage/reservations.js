import {payService} from "../global-js/bootpay.js";
import {reservationService} from "../services/reservation/reservationService.js";
import {showGlobalSelection, showGlobalDialogue, showCustomModal} from "../global-js/global-modal.js";
import {modalLayouts} from "../layouts/mypage/modalLayouts.js";
import {calendarService} from "../global-js/calendar.js";

////
// sort
const selectButton = document.querySelector('.select-box-btn');
const selectButtonImg = selectButton.querySelector('img');
const selectBoxList = document.querySelector('.select-box-list');

const selectBoxItems = document.querySelectorAll('.select-box-list button');

selectButton.addEventListener('click', () => {
    selectBoxList.classList.toggle('show');
    transformSelectBoxImg(selectBoxList.classList.contains('show'));
});

selectBoxItems.forEach(item => {
    item.addEventListener('click', () => {
        const sortType = item.getAttribute('sortType');

        window.location.href = `/mypage/reservations?page=${currentPage}&sort=${sortType}`;
    });
});

const transformSelectBoxImg = (boo) => {
    boo ? selectButtonImg.style.transform = 'rotate(180deg)' : selectButtonImg.style.transform = 'rotate(0deg)';
}

//// 드롭다운
// 예약취소
document.querySelectorAll('.cancel').forEach(cancel => {
    cancel.addEventListener('click', () => {
        const reservationId = cancel.getAttribute('id');
        showGlobalSelection("예약을 취소하시겠습니까?", () => {
            cancelReservation(reservationId);
        });
    });
});

const cancelReservation = async (reservationId) => {
    const {success, message} = await reservationService.cancelReservation(reservationId);

    if (success) {
        showGlobalDialogue(message, () => {
            window.location.reload();
        });
    } else {
        showGlobalDialogue(message);
    }
}

// 예약삭제
document.querySelectorAll('.delete').forEach(dlt => {
    dlt.addEventListener('click', () => {
        const reservationId = dlt.getAttribute('id');
        showGlobalSelection("예약 내역을 삭제하시겠습니까?", () => {
            removeHistory(reservationId);
        });
    });
});

const removeHistory = async (reservationId) => {
    const {success, message} = await reservationService.deleteReservation(reservationId);

    if (success) {
        showGlobalDialogue(message, () => {
            window.location.reload();
        });
    } else {
        showGlobalDialogue(message);
    }
}

////
// 디테일
document.querySelectorAll('.details').forEach(detail => {
    detail.addEventListener('click', () => showDetail(detail));
});

const reservationModal = document.getElementById('reservationModal');

const showDetail = (item) => {
    const index = item.getAttribute('idx');
    const dto = reservationList[index];
    const {checkIn, checkOut} = dto

    modalLayouts.placeReservationDetailModalLayout(dto, calendarService.prevCalendar, calendarService.nextCalendar)
        .then((html) => {
            reservationModal.innerHTML = html;
        })
        .then(() => {
            reservationModal.querySelector('#prevCalendar').addEventListener('click', calendarService.prevCalendar);
            reservationModal.querySelector('#nextCalendar').addEventListener('click', calendarService.nextCalendar);
            calendarService.setEventDates(checkIn, checkOut);
            calendarService.buildCalendar();
        });

    showCustomModal(reservationModal);
}

////
// 결제
document.querySelectorAll('.reservationPay').forEach(pay => {
    pay.addEventListener('click', function () {
        let reservationId = this.getAttribute('data-id');

        payService.requestPlacePaymentSave(reservationId, payService.payItem);
    });
});