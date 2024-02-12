const ticketDetailContainer = document.getElementById('ticketDetail');
const slideContainers = document.querySelectorAll('.mpc-slide-con');

const noItem = document.querySelector('.mpctd-none');
const detailContainer = document.querySelector('.mpctd-container');

const detailTitle = document.getElementById('detailTitle');
const detailAddress = document.getElementById('detailAddress');
const detailAddressDetail = document.getElementById('detailAddressDetail');
const detailDates = document.getElementById('detailDates');
const detailGrades = document.getElementById('detailGrades');


function openTicketDetail(article, ticketId, title, address, addressDetail, startDate, endDate) {
    slideContainers.forEach(each => each.classList.remove('show'));

    const slideContainer = article.querySelector('.mpc-slide-con');
    slideContainer.classList.add('show');

    noItem.classList.add('hide');
    detailContainer.classList.add('show');

    detailTitle.innerHTML = title;
    detailAddress.value = address;
    detailAddressDetail.value = addressDetail;

    let dateDifference = dateDifferenceInDays(new Date(startDate), new Date(endDate));
    let start = new Date(startDate).getDate() + 1;
    dateDifference++;

    let datesHTML = '';
    const date = new Date(startDate);

    for (let i = 0; i < dateDifference; i++) {
        date.setDate(date.getDate() + 1 + i);

        let formattedDate = formatDate(date);

        const isActive = i === 0 ? 'active' : '';
        datesHTML += `<div class="date-item ${isActive}" data-date="${formattedDate}"><span>${start + i}</span></div>`;
    }

    detailDates.innerHTML = datesHTML;

    ticketService.requestGrades(ticketId, startDate, ticketService.loadGrades);



    let dateItems = document.querySelectorAll('.date-item');
    dateItems.forEach(dateItem => {
        dateItem.addEventListener('click', function () {
            dateItems.forEach(item => item.classList.remove('active'));

            this.classList.add('active');

            let date = this.getAttribute('data-date');

            ticketService.requestGrades(ticketId, date, ticketService.loadGrades)
        });
    });
}

const ticketService = (function () {

    function requestGrades(ticketId, date, callback) {
        fetch(`/ticket/api/getGrades?ticketId=${ticketId}&date=${date}`, {
            method: 'GET'
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw response;
                }
            })
            .then(response => {
                if (callback) {
                    return callback(response.data);
                }
            })
            .catch(error => {
                console.error('Error:', error);
            })
    }

    function loadGrades(data) {
        let gradeHTML = '';

        data.forEach(grade => {

            gradeHTML += `
                <tr>
                    <td>${grade.gradeName}</td>
                    <td>${grade.price.toLocaleString()}</td>
                    <td>${grade.sold}</td>
                    <td>${grade.maxPeople}</td>
                </tr>`

        });

        detailGrades.innerHTML = gradeHTML;
    }

    return {
        requestGrades: requestGrades,
        loadGrades: loadGrades
    }
})();

function editTicket() {
    openModal(modalTicket);
}

/////////////////////////////////////////////////////////////

document.querySelectorAll('.ticketItem').forEach(ticketItem => {
    ticketItem.addEventListener('click', function () {
        const ticketId = this.getAttribute('data-id')
        const title = this.getAttribute('data-title');
        const address = this.getAttribute('data-address');
        const addressDetail = this.getAttribute('data-address-detail');
        const startDate = this.getAttribute('data-start-date');
        const endDate = this.getAttribute('data-end-date');

        openTicketDetail(this, ticketId, title, address, addressDetail, startDate, endDate)
    })
})