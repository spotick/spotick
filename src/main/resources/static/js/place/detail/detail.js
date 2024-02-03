let placeId = $('#placeId').val();
let disabledData = null;
let currentIndex = 0; // 현재 활성화된 이미지의 인덱스
let images = $('.modal-img-item');
let imageCount = images.length; // 이미지의 총 개수
let imageWidth = images.width(); // 각 이미지의 너비

$('.nav-item').on('click', function () {
    $('.nav-item').not(this).removeClass('nav-focus');
    $(this).addClass('nav-focus');
});

// 문의 답변 내용 숨기기, 보이기
$('.inquiry-list-wrap').on('click', '.answer-ctr-box', function () {
    $(this).find('img').toggleClass('none');
    $(this).siblings('.answer-box').toggleClass('none');
});

// 사진 클릭시 모달창 띄우기
$('.grid-item').on('click',function (){
    currentIndex = $(this).data('imgidx')-1;
    updateSliderPosition();
    $('.img-modal-container').removeClass('none');
    $('body').css('overflow', 'hidden');
});

// 장소 이미지 모달창 띄우기
$('.more-img-btn').on('click', function () {
    $('.img-modal-container').removeClass('none');
    $('body').css('overflow', 'hidden');
});

// 장소 이미지 모달창 닫기
$('.modal-close').on('click', function () {
    $('.img-modal-container').addClass('none');
    $('body').css('overflow', 'unset');
});



// 이전 버튼 클릭 이벤트
$('.prev').on('click', function () {
    currentIndex = (currentIndex - 1 + imageCount) % imageCount;
    updateSliderPosition();
});

// 다음 버튼 클릭 이벤트
$('.next').on('click', function () {
    currentIndex = (currentIndex + 1) % imageCount;
    updateSliderPosition();
});

// 슬라이더 위치 업데이트
function updateSliderPosition() {
    let translateValue = -currentIndex * imageWidth;
    $('.modal-img-box').css('transform', 'translateX(' + translateValue + 'px)');
    $('.current-img-num').text(currentIndex + 1);
}

// 달력 창 열기,닫기
$('.schedule-box, .calendar-wrap button').on('click', function () {
    $('.select-people-wrap').addClass('none');
    $('.calendar-wrap').toggleClass('none');
});

// 대여인원 설정 창 열기,닫기
$('.people-box, .visitors-btn').on('click', function () {
    $('.select-people-wrap').toggleClass('none');
});

$(`#checkIn`).on('change', function () {
    $('#checkOut').attr('disabled', false);
    setCheckOutTimes();
});


function getNextDay(dateString) {
    let date = new Date(dateString);

    date.setDate(date.getDate() + 1);

    return date;
}

$('.calendar-wrap button').on('click', function () {
    $('.schedule-box p').text(getReservationDateTimeFormat()).css('color', 'black');
    setReservationFormCheckInAndOut();
    let isTrue = reservationFormOk();
    $('.reservation-btn').toggleClass('on', isTrue);
    if (isTrue) {
        calculateAmountAndShow();
    }
});

function calculateAmountAndShow() {
    let checkIn = parseInt($('#checkIn').val().split(' ')[1]);
    let checkOut = parseInt($('#checkOut').val().split(' ')[1]);

    let usageTime = getUsageTime(checkIn, checkOut);
    let placeSurcharge = $('#placeSurcharge').val();
    let visitors = Number($('.visitors').val());
    let defaultPeople = Number($('#placeDefaultPeople').val());
    let surcharge = defaultPeople < visitors
        ? placeSurcharge * (visitors - defaultPeople) : 0;
    let placePrice = $('#placePrice').val();
    let totalAmount = placePrice * usageTime;
    let text = `
        <div class="reservation-time-box">
            <div class="flex-between-align">
                <p class="small-title">총 <span class="usageTime">${usageTime}</span>시간</p>
                <p class="small-title reservation-amount">${totalAmount.toLocaleString()}원</p>
            </div>
            <div class="flex-between-align">
                <p class="sub-title"><span>${placePrice.toLocaleString()}</span>원 x <span class="usageTime">${usageTime}</span>시간</p>
                <p class="sub-title reservation-amount">${totalAmount.toLocaleString()}원</p>
            </div> `;

    if (surcharge > 0) {
        text += `
                    <div class="reservation-people-box">
                        <div class="flex-between-align">
                            <p class="small-title">총인원</p>
                            <p class="small-title">추가 <span class="surcharge-amount">${surcharge.toLocaleString()}</span>원</p>
                        </div>
                        <div class="flex-between-align">
                            <p class="sub-title">기본 <span>${defaultPeople}</span>명 / 예약 <span id="visitors">${visitors}</span>명</p>
                            <div class="surcharge-box flex-align-center">
                                <p>인당 <span>${placeSurcharge}</span></p>
                            </div>
                        </div>
                    </div>`;
    }
    text += `        
            <div class="line"></div>
            <div class="total-reserve-amount-box">
                <div class="flex-between-align">
                    <p class="small-title">총 금액 합계</p>
                    <p class="small-title">
                        <span class="total-amount">${(totalAmount + surcharge).toLocaleString()}</span>원
                    </p>
                </div>
            </div>
    `;

    $('.calculate-box').html(text);
}


// 예약 날짜에 따른 예약 폼 설정
function setReservationFormCheckInAndOut() {
    let checkInTime = $('#checkIn').val();
    let checkOutTime = $('#checkOut').val();
    $('#reservationCheckIn').val(checkInTime);
    $('#reservationCheckOut').val(checkOutTime);
}


function formatDate(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function getReservationDateTimeFormat() {
    let revArr = $('#reservationDate').val().split('-');
    let checkInTime = new Date($('#checkIn').val()).getHours();
    let checkOutTime = new Date($('#checkOut').val()).getHours();
    let checkIn = convertToAmPmFormat(checkInTime);
    let checkOut = convertToAmPmFormat(checkOutTime);
    let usageTime = getUsageTime(checkInTime, checkOutTime);
    return `${revArr[1]}월 ${revArr[2]}일 ${checkIn.padStart(2, '0') + ':00'} ~ ${checkOut.padStart(2, '0') + ':00'} (${usageTime}시간)`;
}

function getUsageTime(checkInTime, checkOutTime) {
    let usageTime = checkOutTime - checkInTime;
    return usageTime <= 0 ? usageTime + 24 : usageTime;
}


$('.plus').on('click', function () {
    let $visitors = $('.visitors');
    let visitNum = $visitors.val();
    if (visitNum >= 99) {
        $visitors.val(99);
        return;
    }
    $visitors.val(++visitNum);
});

$('.minus').on('click', function () {
    let $visitors = $('.visitors');
    let visitNum = $visitors.val();
    if (visitNum <= 1) {
        $visitors.val(1);
        return;
    }
    $visitors.val(--visitNum);
});

$('.visitors-btn').on('click', function () {
    let visitors = $('.visitors').val();
    $('.people-box p').text(`총 ${visitors}명`)
        .css('color', 'black');
    $('#reservationVisitors').val(visitors);

    let isTrue = reservationFormOk();
    $('.reservation-btn').toggleClass('on', isTrue);
    if (isTrue) {
        calculateAmountAndShow();
    }
});

$('.visitors-reset').on('click', function () {
    $('.people-box p').text(`총인원 수를 입력하세요`)
        .css('color', 'rgb(158, 164, 170)');
    $('.visitors').val(1);
    $('.select-people-wrap').addClass('none');
});


function convertToAmPmFormat(hour) {
    let amPm = hour >= 12 ? '오후' : '오전';
    hour = hour % 12;
    hour = hour ? hour : 12; // 시간이 0이면 12로 변환
    return `${amPm} ${hour.toString()}`;
}

// 문의 모달창 띄우기
$('.inquiry-write-btn').on('click', function () {
    let isLoggedIn = $('#isLoggedIn').val();
    if (isLoggedIn === 'false') {
        alert('로그인이 필요한 서비스 입니다');
        location.href = '/user/login';
        return;
    }
    $('.inquiry-modal-container').removeClass('none');
    $('body').css('overflow', 'hidden');
});

// 문의 모달창 취소
$('.inquiry-cancel').on('click', function () {
    $('.inquiry-modal-container').addClass('none');
    $('body').css('overflow', 'unset');
    clearInquiry();
});

$('#inquiryContent').on('input', function () {
    let textLength = $(this).val().length;
    let $letterCount = $('.letter-count');
    $letterCount.find('span').text(textLength);
    $letterCount.toggleClass('over', textLength > 200);
    let isTrue = textLength !== 0 && !$letterCount.hasClass('over');
    $('.inquiry-submit')
        .toggleClass('on', isTrue);
});

$('.inquiry-modal-wrap').on('click', '.inquiry-submit.on', function () {
//    ajax로 문의 작성 처리
    let inquiryTitle = $('#inquiryTitle').val();
    let inquiryContent = $('#inquiryContent').val();

    fetch(`/places/inquiry/v1/register`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            placeId: placeId,
            inquiryTitle: inquiryTitle,
            inquiryContent: inquiryContent
        }),
    }).then(e => e.json())
        .then(e => getInquiryList(1));

    $('.inquiry-modal-container').addClass('none');
    $('body').css('overflow', 'unset');
    clearInquiry();
});


function clearInquiry() {
    $('#inquiryContent').val('');
    $('#inquiryTitle').val('');
    $('.letter-count span').text(0);
    $('.inquiry-submit').removeClass('on');
}


// 북마크 버튼
$('.place-like-btn').on('click', function () {
    let isLoggedIn = $('#isLoggedIn').val();
    if (isLoggedIn === 'false') {
        alert('로그인이 필요한 서비스 입니다');
        location.href = '/user/login';
        return;
    }

    fetch(`/bookmark?placeId=${placeId}`)
        .then(r => r.json())
        .then(isAdded => console.log(isAdded));

    $(this).find('span').toggleClass('none');
});

$('.reservation-submit-box').on('click', '.reservation-btn.on', function () {
    let reservationCheckIn = $('#reservationCheckIn').val();
    let reservationCheckOut = $('#reservationCheckOut').val();

    fetch(`/reservations/v1/availability/check`, {
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            placeId: placeId,
            reservationCheckIn: reservationCheckIn,
            reservationCheckOut: reservationCheckOut
        })
    })
        .then(e => e.json())
        .then(result => {
            if (result) {
                alert('해당시간에 이미 예약이 존재합니다.')
                return;
            }
            $('#reservationForm').submit();
        });


});


function reservationFormOk() {
    let isTrue = true;
    $('#reservationForm>input').each(function () {
        if ($(this).val() === '') {
            isTrue = false;
        }
    });
    return isTrue;
}

getInquiryList(1);

$('.inquiry-list-wrap').on('click', '.pagination-box button', function () {
    let page = $(this).data('page')
    getInquiryList(page);
});


function getInquiryList(page) {

    fetch(`/places/inquiry/v1/${placeId}/list?page=${page}`)
        .then(response => response.json())
        .then(inquiryDisplay);
}

function inquiryDisplay(data) {
    let $target = $('.inquiry-list-wrap');
    let text = '';
    if (data.inquiryPage.empty) {
        text = `<div class="flex-center empty-inquiry">
                            등록된 문의가 없습니다.
                        </div>`;
        $target.html(text);
        return;
    }

    data.inquiryPage.content.forEach(item => {
        text += `
       <div class="inquiry-item-box ">
           <div class="inquiry-question-box flex-column">
               <div class="question-info">
                   <p class="questioner">${item.questionerNickname}</p>
                   <p class="dot">・</p>
                   <p class="question-date">${item.questionDate}</p>
               </div>
               <p class="inquiry-title">${item.inquiryTitle}</p>
               <p class="inquiry-question">${item.inquiryContent}</p>`;

        if (item.inquiryResponse != null) {
            text += `
                            <div class="answer-ctr-box">
                                  <p>답변보기</p>
                                  <img src="https://s3.hourplace.co.kr/web/images/icon/chevron_down_grey.svg" alt="">
                                  <img class="none"
                                       src="https://s3.hourplace.co.kr/web/images/icon/chevron_up_grey.svg" alt="">
                               </div>
                               <div class="answer-box none">
                                   <p class="answer-info">호스트 답변 ・ ${item.inquiryReplyDate}</p>
                                   <p class="answer-content">${item.inquiryResponse}</p>
                               </div>
                               
                       `;
        }
        text += `    </div>
                    </div>
            `;
    });
    text += `
        <div class="pagination-box flex-center">`;
    if (data.pageBlock.startPage > 1) {
        text += `<button data-page="${data.pageBlock.startPage - 1}" type="button" class="page-prev">
                        <img src="https://shareit.kr/_next/static/media/arrow_left_gray074.fa695002.svg" alt="">
                    </button>`;
    }
    for (let i = data.pageBlock.startPage; i <= data.pageBlock.endPage; i++) {
        text += `<button data-page="${i}" class="page-num ${data.pageBlock.currentPage == i ? 'focus' : ''}" type="button">
                ${i}
            </button>`
    }

    if (data.pageBlock.endPage < data.pageBlock.lastPage) {
        text += `
            <button data-page="${data.pageBlock.endPage + 1}" type="button" class="page-next">
                <img src="https://shareit.kr/_next/static/media/arrow_right_gray074.86c7e872.svg"
                     alt="">
            </button>`;
    }
    text += `</div>`;

    $('.inquiry-cnt').text(data.inquiryPage.totalElements);
    $target.html(text);
}

getReservedTimes();

$(`#reservationDate`).on('change', getReservedTimes);

function getReservedTimes() {
    let reservationDate = $('#reservationDate').val();
    $('#checkIn').html(`<option selected disabled>시작 시간</option>`);
    $('#checkOut').html(`<option selected disabled>종료 시간</option>`)
        .attr('disabled', true);
    setCheckInTimes();
    fetch(`/reservations/v1/places/${placeId}/reserved-times?reservationDate=${reservationDate}`)
        .then(r => r.json())
        .then(disableReservedTime);

}


function setCheckInTimes() {
    let $checkIn = $('#checkIn');
    let reserveDate = $('#reservationDate').val();

    $checkIn.children(':not(:first-child)').remove();
    let text = '';

    for (let i = 0; i < 24; i++) {

        text += `
            <option value="${reserveDate} ${i.toString().padStart(2, '0')}:00:00">${convertToAmPmFormat(i)}시</option>
        `;
    }
    $checkIn.append(text);
}

function setCheckOutTimes() {
    let checkIn = parseInt($('#checkIn').val().split(' ')[1]);
    let $checkOut = $('#checkOut');
    let reserveDate = $('#reservationDate').val();

    $checkOut.children(':not(:first-child)').remove();
    let text = '';
    for (let i = 1; i < 24; i++) {
        let time = i + checkIn;
        time = time >= 24 ? time - 24 : time
        if (time === 0) {
            text += `
               <optgroup label="${getNextDay(reserveDate).getDate()}일">
                </optgroup>
            `;
            reserveDate = getNextDateFormat(reserveDate);
        }
        text += `
            <option value="${reserveDate} ${time.toString().padStart(2, '0')}:00:00">${convertToAmPmFormat(time)}시</option>
        `;
    }
    $checkOut.append(text);

    $('#checkOut option').each((i, opt) => {
        let $opt = $(opt);
        let optValue = new Date($opt.val());
        disabledData.forEach(d => {
            let checkIn = new Date(d.checkIn);
            let checkOut = new Date(d.checkOut);
            if (checkIn <= optValue && optValue < checkOut) {
                $opt.attr('disabled', true);
            }
        });
    })

}

function getNextDateFormat(dateString) {
    let date = new Date(dateString);
    date.setDate(date.getDate() + 1);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

function disableReservedTime(data) {
    let $checkInOpts = $('#checkIn option');
    disabledData = data;

    $checkInOpts.each((i, opt) => {
        let $opt = $(opt);
        let optValue = new Date($opt.val());

        data.forEach(d => {
            let checkIn = new Date(d.checkIn);
            let checkOut = new Date(d.checkOut);
            if (checkIn <= optValue && optValue < checkOut) {
                $opt.attr('disabled', true);
            }
        });
    });
}

let reviewPage = 1;
let hasNext = true;
getReviewList();

$('.review-more-btn-box').on('click','.review-more-btn', getReviewList);

function getReviewList() {
    fetch(`/reviews/place/${placeId}/list?page=${reviewPage++}`)
        .then(r => {
            if (!r.ok) {
                throw new Error('리뷰 리스트 조회 실패')
            }
            return r.json();
        }).then(resp => {
        hasNext = !resp.last;
        reviewDisplay(resp.content);
    });
}

function reviewDisplay(reviewList) {
    let text = '';
    if(reviewList.length ===0&&reviewPage===2){
        $('.review-more-btn-box').html(`
            <div class="flex-center empty-review">
               등록된 리뷰가 없습니다.
            </div>
        `);
        return ;
    }    

    reviewList.forEach(review => {
        text += `
             <div class="review-item" data-reviewid="${review.reviewId}">
                <p class="review-writer">${review.userNickname}</p>
                <div class="flex-align">
                    <div class="star-box">
                        <div class="filled-stars" style="width: ${review.score * 20}%">
                            <img class="filled-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_paintYellow056.a8eb6e44.svg"
                                 alt="평점" width="17">
                            <img class="filled-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_paintYellow056.a8eb6e44.svg"
                                 alt="평점" width="17">
                            <img class="filled-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_paintYellow056.a8eb6e44.svg"
                                 alt="평점" width="17">
                            <img class="filled-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_paintYellow056.a8eb6e44.svg"
                                 alt="평점" width="17">
                            <img class="filled-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_paintYellow056.a8eb6e44.svg"
                                 alt="평점" width="17">
                        </div>
                        <div class="default-stars">
                            <img class="default-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_gray084.e69177ff.svg"
                                 alt="">
                            <img class="default-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_gray084.e69177ff.svg"
                                 alt="">
                            <img class="default-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_gray084.e69177ff.svg"
                                 alt="">
                            <img class="default-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_gray084.e69177ff.svg"
                                 alt="">
                            <img class="default-star"
                                 src="https://shareit.kr/_next/static/media/star_filled_gray084.e69177ff.svg"
                                 alt="">
                        </div>
                    </div>
                    <span class="review-score">${review.score}.0</span>
                    <div>
                        <span class="review-write-date">${review.createdDate}</span>
                    </div>
                </div>
                <p class="review-content">${review.content}</p>
             </div>
        `;
    });
    $('.review-list').append(text);

    if (!hasNext) {
        $('.review-more-btn-box').html('');
    }
}

$('.test').on('click',function (){
    let targetPoint = $('#placeRule').position().top+500;
    window.scrollTo({top:targetPoint, left:0, behavior:'smooth'});
});













