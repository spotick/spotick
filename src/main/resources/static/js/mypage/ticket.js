import {loadingMarkService} from "../utils/loadingMark.js";
import {ticketService} from "../services/ticket/ticketService.js";
import {mypageTicketLayout} from "../layouts/ticket/mypage.js";

const loadingMark = document.getElementById('loadingMark');

const slideContainers = document.querySelectorAll('.mpc-slide-con');

const noItem = document.querySelector('.mpctd-none');
const detailContainer = document.querySelector('.mpctd-container');

const detailTitle = document.getElementById('detailTitle');
const detailAddress = document.getElementById('detailAddress');
const detailAddressDetail = document.getElementById('detailAddressDetail');
const detailDates = document.getElementById('detailDates');
const detailGrades = document.getElementById('detailGrades');

// 이미 불러왔었던 티켓 정보를 배열형태로 저장함으로써 불필요한 sql을 최소화 한다. {ticketId, date, gradeInfo}
let gradeData = [];

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
    let start = new Date(startDate).getDate();
    dateDifference++;

    let datesHTML = '';
    const date = new Date(startDate);

    for (let i = 0; i < dateDifference; i++) {
        date.setDate(start + i);

        let formattedDate = formatDate(date);

        const isActive = i === 0 ? 'active' : '';
        datesHTML += `<div class="date-item ${isActive}" data-date="${formattedDate}"><span>${start + i}</span></div>`;
    }

    detailDates.innerHTML = datesHTML;

    loadingMarkService.show(loadingMark)
        .then(() => checkGrade(ticketId, startDate))
        .then(() => loadingMarkService.hide(loadingMark));


    let dateItems = document.querySelectorAll('.date-item');
    dateItems.forEach(dateItem => {
        dateItem.addEventListener('click', function () {
            dateItems.forEach(item => item.classList.remove('active'));

            this.classList.add('active');

            let date = this.getAttribute('data-date');

            checkGrade(ticketId, date);
        });
    });
}

async function checkGrade(ticketId, date) {
    const existingKV = gradeData.find(data => data.ticketId === ticketId && data.date === date);

    if (existingKV) {
        detailGrades.innerHTML = mypageTicketLayout.showGradeInfo(existingKV.grade)
        return;
    }

    try {
        const responseData = await ticketService.getGrades(ticketId, date);
        const newData = {ticketId: ticketId, date: date, grade: responseData};

        // gradeData에 추가
        gradeData.push(newData);

        detailGrades.innerHTML = mypageTicketLayout.showGradeInfo(responseData);
    } catch (error) {
        console.error('Error:', error);
    }
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