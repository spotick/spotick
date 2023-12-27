
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
    $('.modal-container').removeClass('none');
});

// 장소 이미지 모달창 닫기
$('.modal-close').on('click',function (){
    $('.modal-container').addClass('none');
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


































