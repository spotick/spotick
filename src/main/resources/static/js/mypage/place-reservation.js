import {loadingMarkService} from "../utils/loadingMark.js";
import {calendarService} from "../global-js/calendar.js";
import {showGlobalDialogue, showGlobalSelection, showCustomModal} from "../global-js/global-modal.js";
import {sliceReservationListComponent} from "../modules/place/reservationComponent.js";
import {modalLayouts} from "../layouts/mypage/modalLayouts.js";
import {reservationService} from "../services/reservation/reservationService.js";

const placeModal = document.getElementById('placeModal');
const reservationContainer = document.getElementById('reservationsContainer');
const loadingMark = document.getElementById('mpLoadingMark');

let page = 0;
let isLastPage = false;
let isLoading = false;
let contentsSaver = [];

window.onload = () => {
    // 첫 리스트 출력
    getReservationList();

    // 이후 리스트 출력
    window.addEventListener('scroll', function () {
        if (isLoading || !hasNext) return;

        let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

        if (clientHeight + scrollTop >= scrollHeight) {
            isLoading = true;
            reservationService.requestReservations(reservationService.loadReservations);
        }
    });
};


const getReservationList = async () => {
    if (!isLastPage && !isLoading) {
        isLoading = true;
        await loadingMarkService.show(loadingMark);

        const {isLast, html, contents} = await sliceReservationListComponent(pId, page);

        contentsSaver = contentsSaver.concat(contents);
        isLastPage = isLast;

        if (html === '') {
            loadNoList();
        }

        reservationContainer.insertAdjacentHTML("beforeend", html);

        loadingMarkService.hide(loadingMark);
        isLoading = false;
    }
}

function loadNoList() {
    reservationContainer.innerHTML =
        `<div class="mpcp-no-list">
            <span>예약 요청이 없습니다.</span>
         </div>`;
}

reservationContainer.addEventListener('click', (e) => {
    const button = e.target.closest('.detailOpen');
    if (button) {
        const reviewId = parseInt(button.getAttribute('rId'));

        contentsSaver.forEach(content => {
            if (content.id === reviewId) {
                const {checkIn, checkOut} = content;
                placeModal.innerHTML = modalLayouts.reservationRequestModalLayout(content);

                showCustomModal(placeModal);

                calendarService.setEventDates(checkIn, checkOut);
                calendarService.buildCalendar();

                placeModal.querySelector('#nextCalendar').addEventListener('click', calendarService.nextCalendar);
                placeModal.querySelector('#prevCalendar').addEventListener('click', calendarService.prevCalendar);

                const approve = placeModal.querySelector('#approveRequestBtn');
                const reject = placeModal.querySelector('#rejectRequestBtn');

                approve.addEventListener('click', () => {
                    const reservationId = parseInt(approve.parentElement.getAttribute('rId'));

                    showGlobalSelection(
                        "해당 예약 요청을<br>승인하시겠습니까?",
                        () => approveReservation(reservationId),
                        null,
                        false
                    );
                });

                reject.addEventListener('click', () => {
                    const reservationId = parseInt(approve.parentElement.getAttribute('rId'));

                    showGlobalSelection(
                        "해당 예약 요청을<br>거절하시겠습니까?",
                        () => rejectReservation(reservationId),
                        null,
                        false
                    );
                });
            }
        });
    }
});

const approveReservation = async (reservationId) => {
    const {success, message} = await reservationService.approveReservation(reservationId);

    if (success) {
        const reservations = reservationContainer.querySelectorAll('.reservation');
        if (reservations.length === 1) {
            loadNoList();
        }

        reservations.forEach(reservation => {
            const rId = parseInt(reservation.getAttribute('rId'));
            if (rId === reservationId) {
                reservation.remove();
            }
        });

        contentsSaver = contentsSaver.filter(content => content.id !== reservationId);

        showGlobalDialogue(message);
    } else {
        showGlobalDialogue(message);
    }
}

const rejectReservation = async (reservationId) => {
    const {success, message} = await reservationService.rejectReservation(reservationId);

    if (success) {
        const reservations = reservationContainer.querySelectorAll('.reservation');
        if (reservations.length === 1) {
            loadNoList();
        }

        reservations.forEach(reservation => {
            const rId = parseInt(reservation.getAttribute('rId'));
            if (rId === reservationId) {
                reservation.remove();
            }
        });

        contentsSaver = contentsSaver.filter(content => content.id !== reservationId);

        showGlobalDialogue(message);
    } else {
        showGlobalDialogue(message);
    }
}