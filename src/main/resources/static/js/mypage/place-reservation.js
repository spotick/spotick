let page = 0;
let hasNext = true;
let isLoading = false;


const reservationContainer = document.getElementById('reservationsContainer');

const detailImg = document.getElementById('detailImg');
const detailNickname = document.getElementById('detailNickname');
const detailVisitors = document.getElementById('detailVisitors');
const detailAmount = document.getElementById('detailAmount');
const detailContent = document.getElementById('detailContent');
const detailCheckIn = document.getElementById('detailCheckIn');
const detailCheckOut = document.getElementById('detailCheckOut');

const detailId = document.getElementById('detailId');

const reservationService = (function () {

    function requestReservations(callback) {
        loadingMarkService.show();

        const placeId = extractVariableFromURL();

        //todo : 로딩 마스크 테스트용, 테스트 후 삭제 필요
        setTimeout(() => {

            fetch(`/reservation/api/get/${placeId}?page=${page}`, {
                method: 'GET'
            })
                .then(response => {
                    loadingMarkService.hide();
                    if (response.status === 204) {
                        return null;
                    } else if (response.status === 200) {
                        return response.json();
                    } else {
                        throw response;
                    }
                })
                .then(response => {
                    if (response && response.data && response.data.content) {
                        if (response.data.last) {
                            hasNext = false;
                        }
                        callback(response.data.content);
                    } else {
                        loadNoList();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                })
                .finally(() => {
                    isLoading = false;
                })

        }, 500);
    }

    function loadReservations(content) {

        content.forEach(reservation => {

            let checkIn = formatKoreanDate(reservation.checkIn);
            let checkOut = formatKoreanDate(reservation.checkOut);

            let html =
                `<div class="mpcp-item">
                <div class="mpcpi-top">
                    <div class="mpcpi-request-user-con">
                        <div class="mpcpi-ruser-icon">
                            ${reservation.defaultImage ?
                    `<img src="/file/default/display?fileName=${reservation.fileName}">` :
                    `<img src="/file/display?fileName=${reservation.uploadPath}/t_${reservation.uuid}_${reservation.fileName}">`
                }
                        </div>
                        <span class="mpcp-ruser-name">${reservation.nickname}</span>
                    </div>
                </div>
                <div class="mpcp-body">
                    <div class="mpcp-content">
                        <span>${checkIn}</span>
                        <span style="margin: 0 10px;">~</span>
                        <span>${checkOut}</span>
                    </div>
                    <div class="mpcp-btn detailOpen" 
                     data-id="${reservation.id}"
                     data-visitors="${reservation.visitors}"
                     data-content="${reservation.content}" 
                     data-check-in="${reservation.checkIn}" 
                     data-check-out="${reservation.checkOut}" 
                     data-amount="${reservation.amount}" 
                     data-nickname="${reservation.nickname}"
                     ${reservation.defaultImage ?
                        `data-img="/file/default/display?fileName=${reservation.fileName}">` :
                        `data-img="/file/display?fileName=${reservation.uploadPath}/t_${reservation.uuid}_${reservation.fileName}">`
                     }
                        <span>상세보기</span>
                    </div>
                </div>
            </div>`

            reservationContainer.insertAdjacentHTML("beforeend", html);
        })

        document.querySelectorAll('.detailOpen').forEach(detailOpenBtn => {
            detailOpenBtn.addEventListener('click', function () {
                let id = this.getAttribute('data-id')
                let img = this.getAttribute('data-img');
                let nickname = this.getAttribute('data-nickname');
                let visitors = this.getAttribute('data-visitors');
                let amount = this.getAttribute('data-amount');
                let content = this.getAttribute('data-content');
                let checkIn = this.getAttribute('data-check-in');
                let checkOut = this.getAttribute('data-check-out');


                regenerateModalForm(id, img, nickname, visitors, amount, content, checkIn, checkOut);
            })
        })
    }

    function loadNoList() {
        reservationContainer.innerHTML =
            `<div class="mpcp-no-list">
            <span>문의 내역이 없습니다.</span>
         </div>`;
    }

    function regenerateModalForm(id, profileImg, nickname, visitors, amount, content, checkIn, checkOut) {
        detailId.value = id;

        detailImg.src = profileImg;
        detailNickname.innerHTML = nickname;
        detailVisitors.value = visitors;
        detailAmount.value = amount.toLocaleString('ko-KR') + '원';
        detailContent.value = content;

        calendarService.setEventDates(checkIn, checkOut);
        calendarService.buildCalendar();

        detailCheckIn.value = formatKoreanDate(checkIn);
        detailCheckOut.value = formatKoreanDate(checkOut);

        openModal(modalPlace);
    }

    function requestReservationApproval(reservationId) {
        closeModal(globalSelection);

        fetch(`/reservation/api/approve/${reservationId}`, {
            method: 'PATCH'
        })
            .then(response => {
                if (response.ok) {
                    return response.text()
                } else {
                    throw response;
                }
            })
            .then(message => {
                alert(message);
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);

                error.text().then(message => showGlobalDialogue(message));
            });
    }

    function requestReservationRejection(reservationId) {
        closeModal(globalSelection);

        fetch(`/reservation/api/reject/${reservationId}`, {
            method: 'PATCH'
        })
            .then(response => {
                if (response.ok) {
                    return response.text()
                } else {
                    throw response;
                }
            })
            .then(message => {
                alert(message);
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);

                error.text().then(message => showGlobalDialogue(message));
            });
    }

    return {
        requestReservations: requestReservations,
        loadReservations: loadReservations,
        requestReservationApproval: requestReservationApproval,
        requestReservationRejection: requestReservationRejection
    }
})();

//////////////////////////////////////////////////////////////////

window.onload = function () {
    // 첫 리스트 출력
    reservationService.requestReservations(reservationService.loadReservations);

    // 이후 리스트 출력
    window.addEventListener('scroll', function () {
        if (isLoading || !hasNext) return;

        let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

        if (clientHeight + scrollTop >= scrollHeight) {
            isLoading = true;
            page++;
            reservationService.requestReservations(reservationService.loadReservations);
        }
    })
};

document.getElementById('approveRequestBtn').addEventListener('click', function () {
    let reservationId = detailId.value;

    showGlobalSelection(
        "해당 예약 요청을<br>승인하시겠습니까?",
        () => reservationService.requestReservationApproval(reservationId)
    );
})

document.getElementById('rejectRequestBtn').addEventListener('click', function () {
    let reservationId = detailId.value;

    showGlobalSelection(
        "해당 예약 요청을<br>거절하시겠습니까?",
        () => reservationService.requestReservationRejection(reservationId)
    );
})
