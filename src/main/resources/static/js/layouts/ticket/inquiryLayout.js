// 페이저블 데이터를 받아온 뒤 페이지를 가공처리해서 돌려주는 layout
export function ticketInquiryListLayout(pageData) {
    let html = '';

    // 페이저블 데이터를 통해 아무것도 없을 시 문의가 없음을 html로 반환해준다.
    if (pageData.totalElements === 0) {
        html = `<div class="flex-center empty-inquiry">등록된 문의가 없습니다.</div>`;
        return html;
    }

    // 페이저블 데이터를 통해 조회해온 내용을 html로 반환한다
    pageData.content.forEach(item => {
        html +=
            `
               <div class="inquiry-item-box ">
                   <div class="inquiry-question-box flex-column">
                       <div class="question-info">
                           <p class="questioner">${item.questionerNickname}</p>
                           <p class="dot">・</p>
                           <p class="question-date">${item.questionDate}</p>
                       </div>
                       <p class="inquiry-title">${item.inquiryTitle}</p>
                       <p class="inquiry-question">${item.inquiryContent}</p>`
        ;

        if (item.inquiryResponse != null) {
            html +=
                `
                        <div class="answer-ctr-box">
                            <p>답변보기</p>
                            <img src="https://s3.hourplace.co.kr/web/images/icon/chevron_down_grey.svg" alt="">
                            <img class="none" src="https://s3.hourplace.co.kr/web/images/icon/chevron_up_grey.svg" alt="">
                        </div>
                        <div class="answer-box none">
                            <p class="answer-info">호스트 답변 ・ ${item.inquiryReplyDate}</p>
                            <p class="answer-content">${item.inquiryResponse}</p>
                        </div>
                `;
        }

        html +=
            `    
                    </div>
                </div>
            `;
    });

    return html;
}

export function ticketDetailInquiryPaginationComponent(pagination) {
    let html = ``;

    if (pagination.hasPrevBlock) {
        html +=
            `
                <button class="QnaPaginationArrowBtn" type="button" page="${pagination.startPage - 1}"> 
                    <img alt="이전 페이지" class="QnaPaginationArrowBtnImg"
                         src="/imgs/arrow_left_gray074.fa695002.svg"/>
                </button>
            `
    }

    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        if (i === pagination.currentPage) {
            html += `<button class="PaginationPageOn" type="button">${i}</button>`;
        } else {
            html += `<button class="PaginationPageOff" type="button" page="${i}">${i}</button>`;
        }
    }

    if (pagination.hasNextBlock) {
        html +=
            `
                <button class="QnaPaginationArrowBtn" type="button" page="${pagination.endPage + 1}">
                    <img alt="다음 페이지" class="QnaPaginationArrowBtnImg"
                         src="/imgs/arrow_right_gray034.648942ed.svg"/>
                </button>
            `;
    }

    return html;
}

export function ticketInquiryComponent(data) {

}