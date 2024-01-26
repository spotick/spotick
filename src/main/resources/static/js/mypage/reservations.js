// 액션 버튼 컨트롤
const actionBtns = document.querySelectorAll('.mpcr-action-btn');

function showGSForCancelingReservation(reservationId) {
    showGlobalSelection("예약을 취소하시겠습니까?", () => cancelReservation(reservationId))
}

function cancelReservation(reservationId) {
    closeModal();

    fetch('/mypage/reservation/' + reservationId + '/cancel', {
        method: 'GET'
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw response
            }
        })
        .then(message => {
            alert(message);
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);

            error.text().then(message => showGlobalDialogue(message))
        });
}

function showGSForRemoveHistory(reservationId) {
    showGlobalSelection("예약내역을<br>삭제하시겠습니까?", () => removeHistory(reservationId))
}

function removeHistory(reservationId) {
    closeModal();

    fetch('/mypage/reservation/' + reservationId + '/delete', {
        method: 'GET'
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw response
            }
        })
        .then(message => {
            alert(message);
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);

            error.text().then(message => showGlobalDialogue(message))
        });
}


const address = document.getElementById('detailAddress');
const eval = document.getElementById('detailEval');
const bookmark = document.getElementById('detailBookmark');
const title = document.getElementById('detailTitle');
const price = document.getElementById('detailPrice');
const status = document.getElementById('detailStatus');
const visitors = document.getElementById('detailVisitors');
const content = document.getElementById('detailContent');
const checkInC = document.getElementById('detailCheckIn');
const checkOutC = document.getElementById('detailCheckOut');
const img = document.getElementById('detailImg');

function showDetail(item) {
    openModal(modalReservation);

    const evalAvg = item.getAttribute('data-evalAvg');
    const evalCount = item.getAttribute('data-evalCount');
    const checkIn = item.getAttribute('data-checkIn');
    const checkOut = item.getAttribute('data-checkOut');

    address.innerHTML = item.getAttribute('data-address');
    eval.innerHTML = evalAvg + "(" + evalCount + ")";
    bookmark.innerHTML = item.getAttribute('data-bookmarkCount');
    title.innerHTML = item.getAttribute('data-title')
    price.innerHTML = item.getAttribute('data-price')
    status.innerHTML = item.getAttribute('data-status')
    visitors.value = item.getAttribute('data-visitors')
    content.textContent = item.getAttribute('data-content').replace(/<br>/g, '\n');
    checkInC.value = item.getAttribute('data-display-checkIn');
    checkOutC.value = item.getAttribute('data-display-checkOut');
    img.src = item.getAttribute('data-img');

    calendarService.setEventDates(checkIn, checkOut);
    calendarService.buildCalendar();
}