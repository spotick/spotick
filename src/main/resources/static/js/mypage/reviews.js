const contentTextArea = document.getElementById('content');
const typeCounter = document.getElementById('typeCounter');
const mrConfirmBtn = document.getElementById('mrConfirmBtn');
const reviewRating = document.getElementById('score');
const stars = document.querySelectorAll('.mrf-star');
const reservationIdInput = document.getElementById('reservationId');

// 작성하기 버튼 클릭시 아이템 값 넘겨주며 모달창 on
const detailImage = document.getElementById('detailImage');
const detailAddress = document.getElementById('detailAddress');
const detailEval = document.getElementById('detailEval');
const detailBookmark = document.getElementById('detailBookmark');
const detailTitle = document.getElementById('detailTitle');
const detailPrice = document.getElementById('detailPrice');
const detailCheckIn = document.getElementById('detailCheckIn');
const detailCheckOut = document.getElementById('detailCheckOut');
const detailVisitors = document.getElementById('detailVisitors');
const detailContent = document.getElementById('detailContent');
const detailStatus = document.getElementById('detailStatus');

function popupReviewFrom(reservationId, image, address, eval, bookmarkCount, title, price, checkIn, checkOut, visitors, content, status) {
    modalBg.classList.add('show');
    modalReviewForm.classList.add('show');

    reservationIdInput.value = reservationId;
    detailImage.src = "/file/display?fileName=" + image;
    detailAddress.innerHTML = address;
    detailEval.innerHTML = eval;
    detailBookmark.innerHTML = bookmarkCount;
    detailTitle.innerHTML = title;
    detailPrice.innerHTML = price;
    detailCheckIn.innerHTML = checkIn;
    detailCheckOut.innerHTML = checkOut;
    detailVisitors.innerHTML = visitors;
    detailContent.innerHTML = content;
    detailStatus.innerHTML = status;

    mrConfirmBtn.onclick = () => recheckReviewForm();
    contentTextArea.value = '';
    typeCounter.innerHTML = '0';
    reviewRating.value = 1
    stars.forEach(star => {
        star === stars[0] ? star.classList.add('on') : star.classList.remove('on')
    })
    mrConfirmBtn.disabled = true;
}

// 평점 작동

function setReviewScore(score) {
    stars.forEach(star => {
        star.classList.remove('on')
    })
    for (let i = 0; i < score; i++) {
        stars[i].classList.add('on');
    }
    reviewRating.value = score;
}

// 리뷰 작성시 글자수 체크 및 작성버튼 완료 검증
function checkTypeCounts() {
    // 작성 완료 버튼 통제
    mrConfirmBtn.disabled = content.value.length < 10

    // 글자 수 통제
    const maxCharCount = 200;
    if (content.value.length > maxCharCount) {
        content.value = content.value.slice(0, maxCharCount);
    }
    typeCounter.textContent = `${content.value.length}`;
}

// 후기 등록 재확인
function recheckReviewForm() {
    const reservationId = reservationIdInput.value;
    const score = reviewRating.value;
    const content = contentTextArea.value;

    console.log(reservationId)
    console.log(score)
    console.log(content)


    showGlobalSelection("후기를 작성하시겠습니까?", () => postReview(reservationId, score, content))
}

const errorContent = document.getElementById('errorContent');
const errorScore = document.getElementById('errorScore');
// 후기 등록 비동기 통신 처리
function postReview(reservationId, score, content) {
    closeOnlyThisModal(globalSelection);

    const reviewRegisterDto = {
        reservationId: reservationId,
        score: score,
        content: content
    }

    fetch('/reviews/write', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // JSON 형태의 데이터를 전송한다고 명시
        },
        body: JSON.stringify(reviewRegisterDto)
    })
        .then(response => {
            if (response.ok) {
                alert("리뷰가 등록되었습니다.");
                window.location.reload();
            } else {
                throw response
            }
        })
        .catch(error => {
            console.error('Error:', error);

            error.json().then(data => {

                vibrateTarget(modalReviewForm);

                data.errors.forEach(error => {
                    if (error.field === 'content') {
                        errorContent.innerHTML = error.message;
                    } else if (error.field === 'score') {
                        errorScore.innerHTML = error.message;
                    }
                })
            })
        });
}

function recheckReviewableDelete(reservationId) {
    showGlobalSelection("리뷰는 호스트에게<br>큰 힘이 됩니다!<br>그래도 삭제하시겠습니까?", () => deleteReviewable(reservationId));
}

function deleteReviewable(reservationId) {
    fetch('/mypage/reviews/notReviewing/' + reservationId, {
        method: 'PATCH'
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw response
            }
        })
        .then(message => {
            alert(message);
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);

            error.text().then(message => showGlobalDialogue(message))
        });
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////이벤트핸들러
// 작성 버튼
document.querySelectorAll('.reservationWrite').forEach(reservationWrite => {
    reservationWrite.addEventListener('click', function () {
        const reservationId = this.getAttribute('data-reservation-id');
        const image = this.getAttribute('data-image');
        const address = this.getAttribute('data-address');
        const eval = this.getAttribute('data-eval');
        const bookmarkCount = this.getAttribute('data-bookmark-count');
        const title = this.getAttribute('data-title');
        const price = this.getAttribute('data-price');
        const checkIn = this.getAttribute('data-check-in');
        const checkOut = this.getAttribute('data-check-out');
        const visitors = this.getAttribute('data-visitors');
        const content = this.getAttribute('data-content');
        const status = this.getAttribute('data-status');

        popupReviewFrom(reservationId, image, address, eval, bookmarkCount, title, price, checkIn, checkOut, visitors, content, status);
    })
})

// 삭제 버튼
document.querySelectorAll('.reservationDelete').forEach(reservationDelete => {
    reservationDelete.addEventListener('click', function () {
        const reservationId = this.getAttribute('data-reservation-id');

        recheckReviewableDelete(reservationId);
    })
})