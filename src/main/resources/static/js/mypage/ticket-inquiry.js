import {loadingMarkService} from "../utils/loadingMark.js";
import {ticketService} from "../services/ticket/ticketService.js";
import {showGradeInfo} from "../layouts/ticket/mypage/ticketsLayout.js";
import {dateDifferenceInDays, formatDate} from "../utils/timeUtils.js";
import {sliceTicketInquiryListHostComponent} from "../modules/inquiry/InquiryComponent.js";
import {showGlobalSelection, showCustomModal, showGlobalDialogue, closeSingleModal} from "../global-js/global-modal.js";
import {modalLayouts} from "../layouts/mypage/modalLayouts.js";
import {inquiryService} from "../services/inquiry/inquiryService.js";

let page = 0;
let isLastPage = false;
let isLoading = false;
let contentsSaver = [];
let gradeData = [];

const detailDates = document.getElementById('detailDates');
const detailGrades = document.getElementById('detailGrades');
const loadingMark = document.getElementById('mpLoadingMark');
const gradeLoadingMark = document.getElementById('loadingMark');
const inquiriesContainer = document.getElementById('inquiryContainer');

window.onload = async () => {
    getInquiries(ticketId, page);

    detailDates.innerHTML = await setGradeDates(startDate, endDate);
    detailGrades.innerHTML = await checkGrade(ticketId, startDate);

    // 스크롤 이벤트 리스너
    window.addEventListener('scroll', function () {
        if (isLoading || isLastPage) return;

        let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

        if (clientHeight + scrollTop >= scrollHeight) {
            getInquiries(ticketId, page);
        }
    })

    const dateItems = document.querySelectorAll('.date-item');
    dateItems.forEach(dateItem => {
        dateItem.addEventListener('click', async () => {
            dateItems.forEach(item => item.classList.remove('active'));

            dateItem.classList.add('active');

            const date = dateItem.getAttribute('data-date');

            detailGrades.innerHTML = await checkGrade(ticketId, date);
        });
    });
}

const getInquiries = async (ticketId, page) => {
    if (!isLastPage && !isLoading) {
        isLoading = true;
        await loadingMarkService.show(loadingMark);
        const {isLast, html, contents} = await sliceTicketInquiryListHostComponent(ticketId, page);

        contentsSaver = contentsSaver.concat(contents);
        isLastPage = isLast;

        if (html === '') {
            loadNoList();
        }

        inquiriesContainer.insertAdjacentHTML("beforeend", html);

        loadingMarkService.hide(loadingMark);
        isLoading = false;
        page++;
    }
}

function loadNoList() {
    inquiriesContainer.innerHTML =
        `<div class="mpcp-no-list">
            <span>문의가 없습니다.</span>
         </div>`;
}

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
    detailGrades.innerHTML = ``;
    await loadingMarkService.show(gradeLoadingMark);
    const existingKV = gradeData.find(data => data.ticketId === ticketId && data.date === date);

    if (existingKV) {
        loadingMarkService.hide(gradeLoadingMark);
        return showGradeInfo(existingKV.grade);
    }

    const responseData = await ticketService.getGrades(ticketId, date);
    const newData = {ticketId: ticketId, date: date, grade: responseData};

    // gradeData에 추가
    gradeData.push(newData);

    loadingMarkService.hide(gradeLoadingMark);
    return showGradeInfo(responseData);
}

const ticketModal = document.getElementById('ticketModal');
inquiriesContainer.addEventListener('click', (e) => {
    const button = e.target.closest('.detailOpen');
    if (button) {
        const inquiryId = parseInt(button.getAttribute('iId'));

        contentsSaver.forEach(content => {
            if (content.id === inquiryId) {
                ticketModal.innerHTML = modalLayouts.inquiryRequestModalLayout(content);
                generateEventListenerOnModal();

                showCustomModal(ticketModal);
            }
        });
    }
});


function generateEventListenerOnModal() {
    const responseTxArea = ticketModal.querySelector('#response');
    const requestButton = ticketModal.querySelector('#requestBtn');
    const typeCounter = ticketModal.querySelector('#typeCounter');

    responseTxArea.addEventListener('input', () => {
        const maxCharCount = 200;
        if (responseTxArea.value.length > maxCharCount) {
            responseTxArea.value = responseTxArea.value.slice(0, maxCharCount);
        }
        typeCounter.textContent = `${responseTxArea.value.length}`;

        requestButton.disabled = responseTxArea.value.length < 10;
    });

    requestButton.addEventListener('click', () => {
        const inquiryId = parseInt(requestButton.getAttribute('iId'));
        showGlobalSelection(
            "답변을 등록하시겠습니까?",
            () => responseInquiry(ticketId, inquiryId, responseTxArea.value),
            null,
            false
        );
    });
}

const responseInquiry = async (ticketId, inquiryId, response) => {
    const {success, message} = await inquiryService.responseTicketInquiry(ticketId, inquiryId, response);

    if (success) {
        const inquiries = inquiriesContainer.querySelectorAll('.inquiry');
        if (inquiries.length === 1) {
            loadNoList();
        }

        inquiries.forEach(inquiry => {
            const iId = parseInt(inquiry.getAttribute('iId'));
            if (iId === inquiryId) {
                inquiry.remove();
            }
        });

        contentsSaver = contentsSaver.filter(content => content.id !== inquiryId);

        showGlobalDialogue(message);
    } else {
        const errorLine = placeModal.querySelector('#errorContent');
        closeSingleModal("gs");
        errorLine.innerHTML = message;
    }
}