const ticketDetailContainer = document.getElementById('ticketDetail');
const slideContainers = document.querySelectorAll('.mpc-slide-con');

const noItem = document.querySelector('.mpctd-none');
const detailContainer = document.querySelector('.mpctd-container');

const detailTitle = document.getElementById('detailTitle');
const detailAddress = document.getElementById('detailAddress');
const detailAddressDetail = document.getElementById('detailAddressDetail');
const detailDates = document.getElementById('detailDates');
const detailGrades = document.getElementById('detailGrades');


function openTicketDetail(article, title, address, addressDetail, startDate, endDate) {
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

    for (let i = 0; i < dateDifference; i++) {
        datesHTML += `<div class="date-item"><span>${start + i}</span></div>`;
    }

    detailDates.innerHTML = datesHTML;

    let dateItems = document.querySelectorAll('.date-item');
    dateItems.forEach(dateItem => {
        dateItem.addEventListener('click', function () {
            dateItems.forEach(item => item.classList.remove('active'));

            this.classList.add('active');
        });
    });
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
        const startDate = this.getAttribute('data-start-date');
        const endDate = this.getAttribute('data-end-date');


        openTicketDetail(this, title, address, addressDetail, startDate, endDate)
    })
})