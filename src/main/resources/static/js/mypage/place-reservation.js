let page = 0;

const currentPath = window.location.pathname;
const pathSegments = currentPath.split('/');
const placeId = pathSegments[pathSegments.length - 1];


const reservationContainer = document.getElementById('reservationsContainer');

function requestReservations(callback) {
    fetch(`/reservation/api/get/${placeId}?page=${page}`, {
        method: 'GET'
    })
        .then(response => {
            if (response.status === 204) {
                // todo: 없을 시 구축
                console.log("없음");
            } else if (response.status === 200) {
                return response.json();
            } else {
                throw response;
            }
        })
        .then(response => {
            callback(response.data.content);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function loadReservations(content) {

    console.log(content)

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
                        <span class="mpcp-ruser-name">${reservation.nickName}</span>
                    </div>
                </div>
                <div class="mpcp-body">
                    <div class="mpcp-content">
                        <span>${checkIn}</span>
                        <span style="margin: 0 10px;">~</span>
                        <span>${checkOut}</span>
                    </div>
                    <div class="mpcp-btn" onclick="openModal(modalPlace)">
                        <span>상세보기</span>
                    </div>
                </div>
            </div>`

        reservationContainer.insertAdjacentHTML("beforeend", html);
    })

}

function formatKoreanDate(dateString) {
    const date = new Date(dateString);

    return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        hour12: false,
    });
}

//////////////////////////////////////////////////////////////////

window.onload = function () {
    requestReservations(loadReservations);
};

