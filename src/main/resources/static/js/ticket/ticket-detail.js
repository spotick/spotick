import {ticketGradeFetch} from "../modules/ticketGradeFetch.js"
import {requestTicketInquiryList, requestTicketInquiryRegister} from "../modules/inquiryFetch.js"
import {
    ticketInquiryListLayout,
    ticketDetailInquiryPaginationComponent
} from "../layouts/ticket/inquiryLayout.js"
import {requestLike} from "../modules/likeFetch.js"
import {payService} from "../global-js/bootpay.js"

const isLoggedIn = document.getElementById('isLoggedIn');

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
    if (isLoggedIn.value === 'false') {
        const selection = confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?");
        if (selection) {
            location.href = '/user/login';
            return;
        } else {
            return;
        }
    }
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
    $('.inquiry-submit').toggleClass('on', isTrue);
    $('#inquiryRegisterBtn').prop('disabled', !isTrue);
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
        const newData = {date: date, grade: responseData};

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
                <div class="RadioBoxContainer ticketItems" id="${data.gradeId}">
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

const inquiryRegisterBtn = document.getElementById('inquiryRegisterBtn');

window.onload = function () {
    getGrade(startDate);
    displayInquiryPage(1);
    inquiryRegisterBtn.disabled = true;
}

const inquiryTitle = document.getElementById('inquiryTitle');
const inquiryContent = document.getElementById('inquiryContent');
inquiryRegisterBtn.addEventListener('click', () => {


    const inquiryReq = {
        ticketId: ticketId,
        inquiryTitle: inquiryTitle.value,
        inquiryContent: inquiryContent.value,
    }

    requestTicketInquiryRegister(inquiryReq)
        .then((res) => {
            if (res.ok) {
                displayInquiryPage(1);
                $('.inquiry-modal-container').addClass('none');
            }
        })
        .catch((error) => {
            console.error(error);
        });
})

const inquiryPagination = document.getElementById('inquiryPagination');


// 통신과 화면을 각각 js파일들로 분리하여 모듈화 시켰고 하나의 함수로 묶어 사용하는 방식을 사용해보았음.

// 장점 : 파일분류를 더욱 세밀하게 해내어 기존의 복잡한 코드 구조를 조금 더 풀어낼 수 있었음.
// 특히 HTML을 추가하는 코드는 기존의 코드를 난잡하게 만드는 주 원인이 되었으나 js파일로 격리해버림으로써 가독성을 조금이나마 끌어올릴 수 있게됨
// 단점 : 통신과 화면을 동시에 처리하던 기존의 방식에서 이 조차 분할 해내게 되니 전역변수를 서로 공유하지 못한다는 것이 단점으로 자리매김하게 된다.
// 그에 따라 코드를 쓰기전 설계를 더욱 세밀하게 할 필요성이 생기게되었다. js파일들이 많아지게됨으로서 파일명에 따른 js파일 분류를 신경써야 한다.

// 코드의 가독성을 끌어올리기 위한 좋은 시도였으나 여전히 코드의 가독성을 확실하게 끌어올렸다라고 말하기엔 여전히 무리가 있다.
// 어떻게하면 한 페이지에 사용되게 되는 js파일들 간의 유기성을 높이면서 가독성을 끌어올릴 수 있을 지는 계속된 연구가 필요하겠다.
function displayInquiryPage(page) {
    requestTicketInquiryList(ticketId, page)
        .then(data => {
            document.getElementById('inquiryContainer').innerHTML = ticketInquiryListLayout(data.data);
            inquiryPagination.innerHTML = ticketDetailInquiryPaginationComponent(data.pagination);

            Array.from(inquiryPagination.children).forEach(child => {
                child.addEventListener('click', () => {
                    const page = child.getAttribute('page');

                    if (page) {
                        displayInquiryPage(page);
                    }
                });
            });

            document.querySelectorAll('.answer-ctr-box').forEach(btn => {
                btn.addEventListener('click', () => {
                    btn.querySelectorAll('img').forEach(img => {
                        img.classList.toggle('none');
                    });

                    btn.nextElementSibling.classList.toggle('none');
                })
            })
        })
}


const likeBtn = document.getElementById('likeBtn');
likeBtn.addEventListener('click', () => {
    if (isLoggedIn.value === 'false') {
        const selection = confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?");
        if (selection) {
            location.href = '/user/login';
            return;
        } else {
            return;
        }
    }

    const status = likeBtn.getAttribute('data-status');

    requestLike(status, ticketId, (result) => {
        likeBtn.setAttribute('data-status', result);

        changeLike(likeBtn, result);
    });
})

function changeLike(btn, status) {
    // status에 따라서 클래스 변경
    const off = btn.children[0];
    const on = btn.children[1];

    if (status) {
        off.classList.add('none');
        on.classList.remove('none')
    } else {
        off.classList.remove('none');
        on.classList.add('none')
    }
}

document.getElementById('purchase').addEventListener('click', () => {
    const ticketItems = document.querySelectorAll('.ticketItems');
    const selectedDate = document.getElementById('selectedDate').value;
    const ticketOrderDetailDtoList = [];

    // 유저가 선택한 티켓
    ticketItems.forEach(item => {
        const radioBoxInLine = item.querySelector('.RadioBoxInLine');
        const hasOnClass = radioBoxInLine.classList.contains('On');
        if (hasOnClass) {
            const gradeId = item.id;
            const input = item.querySelector('.visitors');
            const quantity = input.value;
            ticketOrderDetailDtoList.push({ gradeId: gradeId, quantity: quantity });
        }
    });

    payService.requestTicketPaymentSave(ticketId, selectedDate, ticketOrderDetailDtoList, payService.payTickets);
});