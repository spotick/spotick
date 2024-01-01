
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
});

// 장소 이미지 모달창 닫기
$('.modal-close').on('click',function (){
    $('.img-modal-container').addClass('none');
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
});

$('.calendar-wrap button').on('click', function (){
    $('.schedule-box p').text(getReservationDateTime()).css('color','black');
});

function getReservationDateTime(){
    let revArr = $('#reservationDate').val().split('-');
    let checkInTime = $('#checkIn').val();
    let checkOutTime = $('#checkOut').val();
    let checkIn = convertToAmPmFormat(checkInTime);
    let checkOut = convertToAmPmFormat(checkOutTime);
    return `${revArr[1]}월 ${revArr[2]}일 ${checkIn} ~ ${checkOut} (${checkOutTime-checkInTime}시간)`;
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
    $('.people-box p').text(`총 ${$('.visitors').val()}명`)
        .css('color','black');
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
    return `${amPm} ${hour.toString().padStart(2, '0')}:00`;
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

// 좋아요 버튼
$('.place-like-btn').on('click',function (){
    $(this).find('span').toggleClass('none');
});



























