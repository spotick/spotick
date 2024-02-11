const ticketDetailContainer = document.getElementById('ticketDetail');
const slideContainers = document.querySelectorAll('.mpc-slide-con');

function openTicketDetail(article, title, address, addressDetail, ticketTypes) {
    slideContainers.forEach(each => each.classList.remove('show'));

    const slideContainer = article.querySelector('.mpc-slide-con');
    slideContainer.classList.add('show');

    ticketDetailContainer.innerHTML = `
        <div class="mpctd-title">
            <p>${title}</p>
        </div>
        
        <p>행사 장소 주소</p>
        <label class="mpctd-input-con" style="margin-bottom: 8px">
            <input readonly type="text" value="${address}">
        </label>
        <label class="mpctd-input-con" style="margin-bottom: 40px">
            <input readonly type="text" value="${addressDetail}">
        </label>
        <p>티켓 상태</p>
        <div class="mpctd-ticket-container" id="ticket-regular">
            <table class="ticket-table">
                <thead>
                    <tr>
                        <td>등급</td>
                        <td>가격</td>
                        <td>판매수</td>
                        <td>총 좌석수</td>
                    </tr>
                </thead>
                <tbody>
                    ${ticketTypes.map(ticketType => `
                        <tr>
                            <td>${ticketType.gradeName}</td>
                            <td>${ticketType.price}</td>
                            <td>0</td>
                            <td>${ticketType.maxPeople}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>`;
}

function editTicket() {
    openModal(modalTicket);
}

/////////////////////////////////////////////////////////////

document.querySelectorAll('.ticketItem').forEach(ticketItem => {
    ticketItem.addEventListener('click', function () {
        const title = this.getAttribute('data-title');
        const address = this.getAttribute('data-address');
        const addressDetail = this.getAttribute('data-address-detail');
        const grades = this.getAttribute('data-grades')

        const gradesJson = JSON.parse(grades);

        console.log(gradesJson)

        openTicketDetail(this, title, address, addressDetail, gradesJson)
    })
})

function changePage(page, viewType) {
    let url = "/mypage/tickets";
    url += '?page=' + page + '&viewType=' + viewType;
    window.location.href = url;
}