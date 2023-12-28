const mrContent = document.getElementById('mrContent');
const typeCounter = document.getElementById('typeCounter');
const mrConfirmBtn = document.getElementById('mrConfirmBtn');
const reviewRating = document.getElementById('reviewRating');
const stars = document.querySelectorAll('.mrf-star');

// 작성하기 버튼 클릭시 아이템 값 넘겨주며 모달창 on
function popupReviewFrom(postId) {
    modalBg.classList.add('show');
    modalReviewForm.classList.add('show');
    mrConfirmBtn.onclick = () => recheckReviewForm(postId);
    mrContent.value = '';
    typeCounter.innerHTML = '0';
    reviewRating.value = 1
    stars.forEach(star => {star === stars[0] ? star.classList.add('on') : star.classList.remove('on')})
    mrConfirmBtn.disabled = true;
}

// 평점 작동

function setReviewScore(score) {
    stars.forEach(star => {star.classList.remove('on')})
    for (let i = 0; i < score; i++) {
        stars[i].classList.add('on');
    }
    reviewRating.value = score;
}

// 리뷰 작성시 글자수 체크 및 작성버튼 완료 검증
function checkTypeCounts() {
    // 작성 완료 버튼 통제
    mrConfirmBtn.disabled = mrContent.value.length < 10

    // 글자 수 통제
    const maxCharCount = 200;
    if (mrContent.value.length > maxCharCount) {
        mrContent.value = mrContent.value.slice(0, maxCharCount);
    }
    typeCounter.textContent = `${mrContent.value.length}`;
}

// 후기 등록 재확인
function recheckReviewForm(postId) {
    globalSelectionContainer.classList.add('show');
    globalSelectionQuestion.innerHTML = "후기를 작성하시겠습니까?"
    globalSelectionConfirm.onclick = () => postReview(postId);
}

// 후기 등록 비동기 통신 처리
function postReview(postId) {
    console.log(postId);
}