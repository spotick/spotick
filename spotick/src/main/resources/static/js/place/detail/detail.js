$('.nav-item').on('click',function (){
    $('.nav-item').not(this).removeClass('nav-focus');
    $(this).addClass('nav-focus');
});

// 문의 답변 내용 숨기기, 보이기
$('.answer-ctr-box').on('click',function (){
   $(this).find('img').toggleClass('none');
   $(this).siblings('.answer-box').toggleClass('none');
});

// 장소 이미지 모달창 띄우기
$('.more-img-btn').on('click',function (){
    $('.img-modal-container').removeClass('none');
    $('body').css('overflow','hidden');
});

// 장소 이미지 모달창 닫기
$('.modal-close').on('click',function (){
    $('.img-modal-container').addClass('none');
    $('body').css('overflow','unset');
});

let currentIndex = 0; // 현재 활성화된 이미지의 인덱스
let images = $('.modal-img-item');
let imageCount = images.length; // 이미지의 총 개수
let imageWidth = images.width(); // 각 이미지의 너비

// 이전 버튼 클릭 이벤트
$('.prev').on('click',function() {
    currentIndex = (currentIndex - 1 + imageCount) % imageCount;
    updateSliderPosition();
});

// 다음 버튼 클릭 이벤트
$('.next').on('click',function() {
    currentIndex = (currentIndex + 1) % imageCount;
    updateSliderPosition();
});

// 슬라이더 위치 업데이트
function updateSliderPosition() {
    let translateValue = -currentIndex * imageWidth;
    $('.modal-img-box').css('transform', 'translateX(' + translateValue + 'px)');
    $('.current-img-num').text(currentIndex+1);
}

// 달력 창 열기,닫기
$('.schedule-box, .calendar-wrap button').on('click', function (){
    $('.select-people-wrap').addClass('none');
    $('.calendar-wrap').toggleClass('none');
});

// 대여인원 설정 창 열기,닫기
$('.people-box, .visitors-btn').on('click', function (){
    $('.select-people-wrap').toggleClass('none');
});

$(`#checkIn`).on('change',function (){
    $('#checkOut').attr('disabled',false);
    setCheckOutTimes();
});

function setCheckOutTimes(){
    let checkIn = Number($('#checkIn').val());
    let $checkOut = $('#checkOut');
    $checkOut.children(':not(:first-child)').remove();
    let text = '';
    for(let i = 1; i<24;i++){
        let time =i+checkIn;
        time = time>=24?time-24:time
        if (time === 0){
            text += `
               <optgroup label="${getNextDay($('#reservationDate').val()).getDate()}일">
                </optgroup>
            `;
        }
        text+=`
            <option value="${time}">${convertToAmPmFormat(time)}시</option>
        `;
    }
    $checkOut.append(text);
}

function getNextDay(dateString) {
    let date = new Date(dateString);

    date.setDate(date.getDate() + 1);

    return date;
}

$('.calendar-wrap button').on('click', function (){
    $('.schedule-box p').text(getReservationDateTimeFormat()).css('color','black');
    setReservationFormCheckInAndOut();
    let isTrue = reservationFormOk();
    $('.reservation-btn').toggleClass('on',isTrue);
    if(isTrue){
        calculateAmountAndShow();
    }
});

function calculateAmountAndShow(){
    // Todo 사용시간과 예약 인원에 따라 계산해서 화면에 보여주기
}



// 예약 날짜에 따른 예약 폼 설정
function setReservationFormCheckInAndOut(){
    let checkInDate = $('#reservationDate').val();
    let checkInTime= $('#checkIn').val();
    let checkOutTime= $('#checkOut').val();
    let isNextDay = Number(checkInTime)>Number(checkOutTime);
    console.log(isNextDay)
    let checkOutDate = isNextDay?formatDate(getNextDay(checkInDate)):checkInDate;
    $('#reservationCheckIn').val(formatDateTime(checkInDate,checkInTime));
    $('#reservationCheckOut').val(formatDateTime(checkOutDate,checkOutTime));
}
function formatDateTime(date, time) {
    return date + ' ' + time.padStart(2, '0') + ':00:00';
}

function formatDate(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function getReservationDateTimeFormat(){
    let revArr = $('#reservationDate').val().split('-');
    let checkInTime = $('#checkIn').val();
    let checkOutTime = $('#checkOut').val();
    let checkIn = convertToAmPmFormat(checkInTime);
    let checkOut = convertToAmPmFormat(checkOutTime);
    let usageTime = checkOutTime-checkInTime;
    return `${revArr[1]}월 ${revArr[2]}일 ${checkIn.padStart(2, '0')+':00'} ~ ${checkOut.padStart(2, '0')+':00'} (${usageTime<=0?usageTime+24:usageTime}시간)`;
}


$('.plus').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= $visitors.val();
    if(visitNum>=99){
        $visitors.val(99);
        return;
    }
    $visitors.val(++visitNum);
});

$('.minus').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= $visitors.val();
    if(visitNum<=1){
        $visitors.val(1);
        return;
    }
    $visitors.val(--visitNum);
});

$('.visitors-btn').on('click',function (){
    let visitors = $('.visitors').val();
    $('.people-box p').text(`총 ${visitors}명`)
        .css('color','black');
    $('#reservationVisitors').val(visitors);

    let isTrue = reservationFormOk();
    $('.reservation-btn').toggleClass('on',isTrue);
    if(isTrue){
        calculateAmountAndShow();
    }
});

$('.visitors-reset').on('click',function (){
    $('.people-box p').text(`총인원 수를 입력하세요`)
        .css('color','rgb(158, 164, 170)');
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
$('.inquiry-write-btn').on('click',function (){
    $('.inquiry-modal-container').removeClass('none');
});

// 문의 모달창 취소
$('.inquiry-cancel').on('click',function (){
    $('.inquiry-modal-container').addClass('none');
    clearInquiry();
});

$('#inquiryContent').on('input',function (){
    let textLength = $(this).val().length;
    let $letterCount = $('.letter-count');
    $letterCount.find('span').text(textLength);
    $letterCount.toggleClass('over', textLength>200);
    let isTrue =textLength!==0&&!$letterCount.hasClass('over');
    $('.inquiry-submit')
        .toggleClass('on', isTrue);
});

$('.inquiry-modal-wrap').on('click','.inquiry-submit.on',function (){
//    ajax로 문의 작성 처리
    alert('문의작성 완료');
    $('.inquiry-modal-container').addClass('none');
    clearInquiry();
});


function clearInquiry(){
    $('#inquiryContent').val('');
    $('.letter-count span').text(0);
    $('.inquiry-submit').removeClass('on');
}

// 북마크 버튼
$('.place-like-btn').on('click',function (){
    $(this).find('span').toggleClass('none');
});

$('.reservation-submit-box').on('click','.reservation-btn.on',function (){
   $('#reservationForm').submit();
});


function reservationFormOk(){
    let isTrue = true;
    $('#reservationForm>input').each(function (){
        if($(this).val()===''){
            isTrue =false;
        }
    });
    return isTrue;
}























