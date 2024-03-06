import {ticketGradeFetch} from "../modules/ticketGradeFetch.js"


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
document.querySelector(".LikeWithReservationButton").addEventListener("click", function () {
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
$('.QnaMainBtn').on('click', function () {
    $('.inquiry-modal-container').removeClass('none');
});

// 문의 모달창 취소
$('.inquiry-cancel').on('click', function () {
    $('.inquiry-modal-container').addClass('none');
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
    alert('문의작성 완료');
    $('.inquiry-modal-container').addClass('none');
    clearInquiry();
});

function clearInquiry() {
    $('#inquiryContent').val('');
    $('.letter-count span').text(0);
    $('.inquiry-submit').removeClass('on');
}

function adjustVisitorCount(container, operation, maxCount) {
    const inputField = container.querySelector('.visitors');

    let newValue;
    if (operation === 'increment') {
        newValue = Math.min(Number(inputField.value) + 1, maxCount);
    } else if (operation === 'decrement') {
        newValue = Math.max(Number(inputField.value) - 1, 1);
    }

    inputField.value = newValue;
}


// 공유하기 버튼
shareBtn.addEventListener("click", function () {
    document.body.style.overflow = "hidden";
    document.querySelector(".ModalBackground").classList.add("On")
})

modalCloseBtn.addEventListener("click", function () {
    document.body.style.overflow = "auto";
    document.querySelector(".ModalBackground").classList.remove("On")
})

modalBackground.addEventListener("click", function () {
    document.body.style.overflow = "auto";
    document.querySelector(".ModalBackground").classList.remove("On")
})


/////////////////////////////////////////////////////////////////
let gradeData = [];
const ticketId = document.getElementById('id').value;
const startDate = document.getElementById('startDate').value;

export async function getGrade(date) {
    const existingKV = gradeData.find(data => data.date === date);

    if (existingKV) {
        console.log(existingKV.grade);
        loadGradeList(existingKV.grade)
        return;
    }

    try {
        console.log('없어서 실행')
        const responseData = await ticketGradeFetch(ticketId, date);
        const newData = { date: date, grade: responseData };

        // gradeData에 추가
        gradeData.push(newData);

        console.log(responseData);
        loadGradeList(responseData);
    } catch (error) {
        console.error('Error:', error);
    }
}

const gradeSelectContainer = document.getElementById('gradeSelectContainer');
function loadGradeList(dataList) {
    let html = ``;

    dataList.forEach(data => {
        html +=
            `
                <div class="RadioBoxContainer">
                    <input class="totalPrice" type="hidden">
                    <div class="RadioBox">
                        <div class="RadioBoxOutLine">
                            <div class="RadioBoxInLine"></div>
                        </div>
                        <input name="" type="radio">
                        <div class="GoodsInfo">
                            <div class="GoodsInfoTitleContainer">
                                <span class="GoodsInfoTitle">${data.gradeName}<br>(남은인원 ${data.maxPeople - data.sold}명)</span>
                            </div>
                            <div>
                                <p class="GoodsInfoDiscountedPrice">${data.price.toLocaleString()}원</p>
                            </div>
                        </div>
                    </div>
                    <div class="flex-align countWrap" max="${data.maxPeople - data.sold}">
                        <button class="minus" type="button">-</button>
                        <div class="reservation-visitors flex-align-center">
                            <input class="visitors" max="${data.maxPeople - data.sold}" min="1" type="number" value="1">
                        </div>
                        <button class="plus" type="button">+</button>
                    </div>
                </div>
            `;
    })

    gradeSelectContainer.innerHTML = html;


    document.querySelectorAll('.RadioBox').forEach(box => {
        box.addEventListener('click', () => {
            const radioBoxInLine = box.querySelector('.RadioBoxInLine');

            if (radioBoxInLine) {
                radioBoxInLine.classList.toggle('On');
            }
        })
    })

    document.querySelectorAll('.countWrap').forEach(countWrap => {
        const minus = countWrap.querySelector('.minus');
        const plus = countWrap.querySelector('.plus');
        const max = countWrap.getAttribute('max');

        minus.addEventListener('click', () => {
            adjustVisitorCount(countWrap, 'decrement');
        })

        plus.addEventListener('click', () => {
            adjustVisitorCount(countWrap, 'increment', max);
        })
    })
}

window.onload = function () {
    getGrade(startDate)
}

