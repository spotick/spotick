// 모든 QnA 아이템을 선택합니다.
const qnaItems = document.querySelectorAll('.QnaItemContainer');



// 공유하기 버튼 모달
const shareBtn = document.querySelector(".BasicInformationShareBtn")
const modalCloseBtn = document.querySelector(".ShareModalCloseBtn")
const modalBackground = document.querySelector(".ModalBackground")

// 상단 탭 버튼 클릭시 ( 화면 스크롤기능 아직 없음 )
function changeTab(clickedButton) {
    // 모든 버튼에서 InfoTabBtnOn 클래스 제거, InfoTabBtnOff 클래스 추가
    document.querySelectorAll('.InfoTabBtn').forEach(function (button) {
        button.classList.remove('InfoTabBtnOn');
        button.classList.add('InfoTabBtnOff');
    });

    // 클릭된 버튼에 InfoTabBtnOn 클래스 추가, InfoTabBtnOff 클래스 제거
    clickedButton.classList.remove('InfoTabBtnOff');
    clickedButton.classList.add('InfoTabBtnOn');
}

// 문의하기 클릭시 화살표 이미지 변경, 답글 나오게
// 각 QnA 아이템에 대해 이벤트 리스너를 등록합니다.
qnaItems.forEach(function (qnaItem) {
    // "더보기" 버튼을 찾습니다.
    var moreBtn = qnaItem.querySelector('.QnaItemContentsMoreBtn');

    // "더보기" 버튼에 클릭 이벤트를 등록합니다.
    moreBtn.addEventListener('click', function () {
        // 이미지를 토글합니다.
        var moreBtnImg = qnaItem.querySelector('.QnaItemContentsMoreBtnImg');
        toggleImageSrc(moreBtnImg);

        // 답변칸을 토글합니다.
        var repliesContainer = qnaItem.querySelector('.QnaRepliesContainer');
        toggleVisibility(repliesContainer);
    });
});

// 이미지의 src를 토글하는 함수
function toggleImageSrc(img) {
    // 현재 이미지 src에 따라 토글합니다.
    img.src = img.src.includes('down') ? '../../static/imgs/arrow_up_gray074.f2ebf2a9.svg' : '../../static/imgs/arrow_down_gray074.3a483a93.svg';

}
const likeBtnImg = document.querySelector(".LikeWithReservationButtonImg")
document.querySelector(".LikeWithReservationButton").addEventListener("click",function (){
    likeBtnImg.src = likeBtnImg.src.includes('filled') ? '../../static/imgs/heart_2line_gray064.40cc6c61.svg' : '../../static/imgs/heart_filled_sweetBlue046.76e646e1.svg';

})

// 요소의 가시성을 토글하는 함수
function toggleVisibility(element) {
    // "none" 클래스의 존재 여부에 따라 토글합니다.
    if (element.classList.contains('none')) {
        element.classList.remove('none');
    } else {
        element.classList.add('none');
    }
}

// 가격 클릭시
$('.RadioBox.On').click(function () {
    $(this).find('.RadioBoxInLine').toggleClass('On');
});


// 문의 모달창 띄우기
$('.QnaMainBtn').on('click',function (){
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

function adjustVisitorCount(container, operation) {
    // container 요소 내에서 visitors 클래스를 가진 input 요소를 찾습니다.
    const inputField = container.querySelector('.visitors');

    // 현재 값과 연산에 따라 새로운 값을 계산합니다.
    let newValue;
    if (operation === 'increment') {
        newValue = Math.min(Number(inputField.value) + 1, 99);
    } else if (operation === 'decrement') {
        newValue = Math.max(Number(inputField.value) - 1, 1);
    }

    // 새로운 값으로 입력란을 업데이트합니다.
    inputField.value = newValue;
}


// 공유하기 버튼
shareBtn.addEventListener("click",function (){
    document.body.style.overflow = "hidden";
    document.querySelector(".ModalBackground").classList.add("On")
})

modalCloseBtn.addEventListener("click",function (){
    document.body.style.overflow = "auto";
    document.querySelector(".ModalBackground").classList.remove("On")
})

modalBackground.addEventListener("click",function (){
    document.body.style.overflow = "auto";
    document.querySelector(".ModalBackground").classList.remove("On")
})


// 리뷰 쪽====================================================================================================
// 초기 상태 설정
// const arrowDownSrc = "../../static/imgs/promotion-ticket/arrow_down_gray014.f502da9d.svg";
// const arrowUpSrc = "../../static/imgs/promotion-ticket/arrow_up_gray014.75d8599e.svg";
// const reviewSelectBoxBtn = document.querySelector(".ReviewSelectBoxBtn");
// const reviewSelectBoxBtnImg = document.querySelector(".ReviewSelectBoxBtnImg");
// const reviewSelectBoxList = document.querySelector(".ReviewSelectBoxList");
//
// const buttons = document.querySelectorAll('.ReviewSelectBoxList button');
// buttons.forEach(function (btn) {
//     btn.addEventListener('click', function () {
//         // 아이템을 클릭했을 때의 동작
//         buttons.forEach(function (innerBtn) {
//             innerBtn.classList.remove('ReviewFilterSortBtnOn');
//             innerBtn.classList.add('ReviewFilterSortBtnOff');
//         });
//         btn.classList.add('ReviewFilterSortBtnOn');
//         document.querySelector('.ReviewSelectBoxText').textContent = btn.textContent;
//         closeReviewSelectBox();
//     });
// });
//
// reviewSelectBoxBtn.addEventListener("click", function () {
//     // ReviewSelectBoxBtn을 클릭했을 때의 동작
//     if (reviewSelectBoxList.style.display === 'none') {
//         reviewSelectBoxList.style.display = 'flex';
//         reviewSelectBoxBtnImg.src = arrowUpSrc;
//     } else {
//         closeReviewSelectBox();
//     }
// });
//
// function closeReviewSelectBox() {
//     // ReviewSelectBoxList를 닫을 때의 동작
//     reviewSelectBoxList.style.display = 'none';
//     reviewSelectBoxBtnImg.src = arrowDownSrc;
// }
// ====================================================================================================================

// 기존 모달창쪽 ========================================================================================================
// 초기 상태 설정
// var modalArrowDownSrc = "../../static/imgs/promotion-ticket/arrow_down_gray074.3a483a93.svg";
// var modalArrowUpSrc = "../../static/imgs/promotion-ticket/arrow_up_gray074.f2ebf2a9.svg";
// var postQnaModalDropdownBtn = document.querySelector(".PostQnaModalDropdownSelectedBox");
// var postQnaModalDropdownBtnImg = document.querySelector(".PostQnaModalDropdownImg");
// var postQnaModalDropdownList = document.querySelector(".PostQnaModalDropdownSpaceList");
//
// var modalButtons = document.querySelectorAll('.PostQnaModalDropdownSpaceList button');
// modalButtons.forEach(function (btn) {
//     btn.addEventListener('click', function () {
//
//         // 아이템을 클릭했을 때의 동작
//         modalButtons.forEach(function (innerBtn) {
//             innerBtn.classList.remove('ReviewFilterSortBtnOn');
//             innerBtn.classList.add('ReviewFilterSortBtnOff');
//         });
//         btn.classList.add('ReviewFilterSortBtnOn');
//         var selectedText = btn.textContent;
//         document.querySelector('.PostQnaModalDropdownSelectedBox p').textContent = selectedText;
//         document.querySelector('.PostQnaModalDropdownSelectedBox p').style.color = 'black'; // 선택된 버튼의 부모 요소인 p의 글자색을 검정색으로 변경
//         closePostQnaModalDropdown();
//     });
// });
//
// postQnaModalDropdownBtn.addEventListener("click", function () {
//     // PostQnaModalDropdownBtn을 클릭했을 때의 동작
//     if (postQnaModalDropdownList.style.display === 'none') {
//         postQnaModalDropdownList.style.display = 'flex';
//         postQnaModalDropdownBtnImg.src = modalArrowUpSrc;
//     } else {
//         closePostQnaModalDropdown();
//     }
// });
//
// function closePostQnaModalDropdown() {
//     // PostQnaModalDropdownList를 닫을 때의 동작
//     postQnaModalDropdownList.style.display = 'none';
//     postQnaModalDropdownBtnImg.src = modalArrowDownSrc;
// }


// function updateTextLength() {
//     var textarea = document.querySelector('.TextArea');
//     var textLength = textarea.value.length;
//     var maxLength = parseInt(textarea.getAttribute('maxlength'));
//     var textLengthElement = document.querySelector('.PostQnaModalTextLength');
//     textLengthElement.textContent = textLength + '/' + maxLength;
// }

// 문의하기 모달창 켜기
// document.querySelector(".QnaMainBtn").addEventListener("click", function(){
//     document.querySelector(".PostQnaModalContainer").classList.remove("none");
//     document.body.style.overflow = "hidden";
// })
//
// // 문의하기 모달창 끄기
// document.querySelector(".PostQnaModalCrossImg").addEventListener("click", function(){
//     document.querySelector(".PostQnaModalContainer").classList.add("none");
//     document.body.style.overflow = "auto";
// })
//
// document.querySelector(".PostQnaModalFooterBtnCancel").addEventListener("click", function(){
//     document.querySelector(".PostQnaModalContainer").classList.add("none");
//     document.body.style.overflow = "auto";
// })
// 기존 모달창쪽 ========================================================================================================

// var swiper = new Swiper(".swiper", {
//   pagination: {
//     el: ".swiper-pagination",
//     clickable: true,
//   },
//   navigation: {
//     nextEl: ".ImageModalNextvArrowBtn",
//     prevEl: ".ImageModalPrevArrowBtn",
//   },
//   on: {
//         slideChange: function () {
//           document.querySelector(".snapIndex").textContent = swiper.snapIndex + 1;
//         },
//       },
//
// });
//
// // MoreImagesBtn 버튼 요소 선택
// var ImageCount = document.querySelector('.ImageCount');
// var ImageCount2 = document.querySelector('.ImageCount2');
//
//
// // ImageLength 클래스를 가진 모든 요소 선택
// var imageLengthElements = document.querySelectorAll('.ImageLength');
//
// // 각 ImageLength 클래스 내의 div 개수를 합산
// var totalDivCount = Array.from(imageLengthElements).reduce(function (total, element) {
//   return total + element.querySelectorAll('div').length;
// }, 0);
//
// // MoreImagesBtn 버튼 텍스트에 totalDivCount 값을 설정
// ImageCount.textContent =  totalDivCount;
// ImageCount2.textContent =  totalDivCount;
//
// // 이미지 모달창 켜기
// document.querySelector(".MoreImagesBtn").addEventListener("click", function(){
//     document.querySelector(".ImageModalContainer").classList.remove("none");
// })
//
// // 이미지 모달창 끄기
// document.querySelector(".ImageModalCloseBtn").addEventListener("click", function(){
//     document.querySelector(".ImageModalContainer").classList.add("none");
// })

// // 문의하기 모달창 켜기
// document.querySelector(".QnaMainBtn").addEventListener("click", function(){
//     document.querySelector(".PostQnaModalContainer").classList.remove("none");
//     document.body.style.overflow = "hidden";
// })
//
// // 문의하기 모달창 끄기
// document.querySelector(".PostQnaModalCrossImg").addEventListener("click", function(){
//     document.querySelector(".PostQnaModalContainer").classList.add("none");
//     document.body.style.overflow = "auto";
// })
//
// document.querySelector(".PostQnaModalFooterBtnCancel").addEventListener("click", function(){
//     document.querySelector(".PostQnaModalContainer").classList.add("none");
//     document.body.style.overflow = "auto";
// })