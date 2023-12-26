
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



































