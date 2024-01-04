const ticketDetailContainer = document.getElementById('ticketDetail');
const slideContainers = document.querySelectorAll('.mpc-slide-con');

function openTicketDetail(article, title, subTitle, address, addressDetail, ticketTypes) {
    slideContainers.forEach(each => each.classList.remove('show'));

    const slideContainer = article.querySelector('.mpc-slide-con');
    slideContainer.classList.add('show');

    ticketDetailContainer.innerHTML = `
        <div class="mpctd-title">
            <p>${title}</p>
        </div>
        <div class="mpctd-subtitle">
            <p>${subTitle}</p>
            
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
                            <td>${ticketType.name}</td>
                            <td>${ticketType.price}</td>
                            <td>${ticketType.sold}</td>
                            <td>${ticketType.totalSeats}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>`;
}

function editTicket() {
    openModal(modalTicket);
}

// 테스트용
const ticketTypes = [
    { name: '일반석', price: '50,000', sold: '10', totalSeats: '100' },
    { name: 'VIP', price: '70,000', sold: '2', totalSeats: '20' },
    { name: '스탠딩', price: '40,000', sold: '2', totalSeats: '30' },
    { name: '테스트', price: '20,000', sold: '10', totalSeats: '30' }
];