import {loadingMarkService} from "../utils/loadingMark.js";
import {ticketService} from "../services/ticket/ticketService.js";
import {mypageTicketLayout} from "../layouts/ticket/mypage/ticketsLayout.js";
import {showGradeInfo} from "../layouts/ticket/mypage/ticketsLayout.js";
import {dateDifferenceInDays, formatDate} from "../utils/timeUtils.js";

// 이미 불러왔었던 티켓 정보를 배열형태로 저장함으로써 불필요한 sql을 최소화 한다. {ticketId, date, gradeInfo}
let gradeData = [];

const slideContainers = document.querySelectorAll('.mpc-slide-con');
const detailContainer = document.getElementById('detailContainer');

document.querySelectorAll('.ticketItem').forEach(ticket => {
    ticket.addEventListener('click', async () => {
        // 슬라이드
        slideContainers.forEach(each => each.classList.remove('show'));
        ticket.querySelector('.mpc-slide-con').classList.add('show');

        // 우측 레이아웃 제작
        const index = ticket.getAttribute('idx');
        const dto = contents[index];
        const {ticketId, startDate, endDate} = dto;
        detailContainer.innerHTML = await mypageTicketLayout(dto);

        const detailDates = detailContainer.querySelector('#detailDates');
        const detailGrades = detailContainer.querySelector('#detailGrades');
        const loadingMark = detailContainer.querySelector('#loadingMark');

        await loadingMarkService.show(loadingMark);
        detailDates.innerHTML = await setGradeDates(startDate, endDate);
        detailGrades.innerHTML = await checkGrade(ticketId, startDate);
        loadingMarkService.hide(loadingMark);

        // 날짜칸에 이벤트리스너 등록
        const dateItems = detailContainer.querySelectorAll('.date-item');
        dateItems.forEach(dateItem => {
            dateItem.addEventListener('click', async () => {
                dateItems.forEach(item => item.classList.remove('active'));
                dateItem.classList.add('active');

                const date = dateItem.getAttribute('data-date');
                detailGrades.innerHTML = ``;
                await loadingMarkService.show(loadingMark);
                detailGrades.innerHTML = await checkGrade(ticketId, date);
                loadingMarkService.hide(loadingMark);
            });
        });
    });
});

const setGradeDates = async (startDate, endDate) => {
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

    return datesHTML;
}

const checkGrade = async (ticketId, date) => {
    const existingKV = gradeData.find(data => data.ticketId === ticketId && data.date === date);

    if (existingKV) {
        return showGradeInfo(existingKV.grade);
    }

    const responseData = await ticketService.getGrades(ticketId, date);
    const newData = {ticketId: ticketId, date: date, grade: responseData};

    // gradeData에 추가
    gradeData.push(newData);

    return showGradeInfo(responseData);
}