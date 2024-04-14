import {loadingMarkService} from "../modules/loadingMark.js";
import {ticketService} from "../services/ticket/ticketService.js";
import {mypageTicketLayout} from "../layouts/ticket/mypage.js";

let page = 0;
let hasNext = true;
let isLoading = false;

const startDate = document.getElementById('startDate').value;
const endDate = document.getElementById('endDate').value;
const detailDates = document.getElementById('detailDates');
const detailGrades = document.getElementById('detailGrades');

const img = document.getElementById('detailProfileImg');
const nickname = document.getElementById('detailNickName');
const contentArea = document.getElementById('detailContent');

const inquiryIdInput = document.getElementById('inquiryId');
const responseInput = document.getElementById('response');

const errorContent = document.querySelector('.error-content');

const inquiryContainer = document.getElementById('inquiryContainer');

const loadingMark = document.getElementById('mpLoadingMark');
const loadingMarkGrade = document.getElementById('loadingMark');
const ticketId = document.getElementById('id').value;

const inquiryService = (function () {

    function requestInquiries(callback) {
        loadingMarkService.show(loadingMark);

        //todo : 로딩 마스크 테스트용, 테스트 후 삭제 필요
        setTimeout(() => {

            fetch(`/inquiries/api/getTicket/${ticketId}?page=${page}`, {
                method: 'GET'
            })
                .then(response => {
                    loadingMarkService.hide(loadingMark);
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
                        hasNext = false;
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

    function loadInquiries(content) {
        content.forEach(inquiry => {

            let html =
                `<div class="mpcp-item">
                    <div class="mpcpi-top">
                        <div class="mpcpi-request-user-con">
                            <div class="mpcpi-ruser-icon">
                                ${inquiry.defaultImage ?
                    `<img src="/file/default/display?fileName=${inquiry.fileName}">` :
                    `<img src="/file/display?fileName=${inquiry.uploadPath}/t_${inquiry.uuid}_${inquiry.fileName}">`
                }
                            </div>
                            <span class="mpcp-ruser-name">${inquiry.nickname}</span>
                        </div>
                    </div>
                    <div class="mpcp-body">
                        <div class="mpcp-content" style="font-size: 18px;">
                            <span>${inquiry.inquiryTitle}</span>
                        </div>
                        <div class="mpcp-btn detailInquiryBtn"
                             data-content="${inquiry.content}"
                             data-id="${inquiry.id}"
                             data-nickname="${inquiry.nickname}"
                             data-title="${inquiry.inquiryTitle}"
                             ${inquiry.defaultImage ?
                    `data-img="/file/default/display?fileName=${inquiry.fileName}">` :
                    `data-img="/file/display?fileName=${inquiry.uploadPath}/t_${inquiry.uuid}_${inquiry.fileName}">`
                }
                            <span>상세보기</span>
                        </div>
                    </div>
                </div>`;

            inquiryContainer.insertAdjacentHTML("beforeend", html);
        });

        document.querySelectorAll('.detailInquiryBtn').forEach(inquiryBtn => {
            inquiryBtn.addEventListener('click', function () {
                img.src = this.getAttribute('data-img');
                nickname.innerHTML = this.getAttribute('data-nickname');
                contentArea.value = this.getAttribute('data-content');
                inquiryIdInput.value = this.getAttribute('data-id');

                responseInput.value = '';
                errorContent.innerHTML = '';

                openModal(modalPlace);
            });
        });
    }

    function requestUploadResponse(ticketId, inquiryId, responseString) {
        closeOnlyThisModal(globalSelection);

        const inquiryResponse = {
            id: ticketId,
            inquiryId: inquiryId,
            response: responseString
        }

        fetch('/inquiries/api/responseTicketInquiry', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(inquiryResponse)
        })
            .then(response => {
                if (response.ok) {
                    alert("답변이 등록되었습니다.");
                    window.location.reload();
                } else {
                    throw response
                }
            })
            .catch(error => {
                console.error('Error:', error);

                error.text().then(message => {

                    vibrateTarget(modalPlace);

                    errorContent.innerHTML = message;
                })
            });
    }

    function loadNoList() {
        inquiryContainer.innerHTML =
            `<div class="mpcp-no-list">
            <span>문의 내역이 없습니다.</span>
         </div>`;
    }

    return {
        requestInquiries: requestInquiries,
        loadInquiries: loadInquiries,
        requestUploadResponse: requestUploadResponse
    }
})();

let gradeData = [];
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

///////////////////////////////////////////////////////////////////////////////

window.onload = function () {
    // 문의 내역 화면 로드
    inquiryService.requestInquiries(inquiryService.loadInquiries);

    // 날짜 기능 추가
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

    // 날짜의 첫번째 grade 로드
    loadingMarkService.show(loadingMarkGrade)
        .then(() => checkGrade(ticketId, startDate))
        .then(() => loadingMarkService.hide(loadingMarkGrade));

    // 스크롤 이벤트 리스너
    window.addEventListener('scroll', function () {
        if (isLoading || !hasNext) return;

        let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

        if (clientHeight + scrollTop >= scrollHeight) {
            isLoading = true;
            page++;
            inquiryService.requestInquiries(inquiryService.loadInquiries);
        }
    })

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

document.getElementById('requestBtn').addEventListener('click', function () {
    let inquiryId = inquiryIdInput.value;
    let responseString = responseInput.value;


    showGlobalSelection(
        "답변을 등록하시겠습니까?",
        () => inquiryService.requestUploadResponse(ticketId, inquiryId, responseString)
    );
})