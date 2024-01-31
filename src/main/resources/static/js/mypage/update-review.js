const reviewIdInput = document.getElementById('idInput');
const scoreInput = document.getElementById('scoreInput');
const contentArea =document.getElementById('contentArea');
const stars = document.querySelectorAll('.mrf-star');

const mrConfirmBtn = document.getElementById('mrConfirmBtn');
const typeCounter = document.getElementById('typeCounter');

const errorContent = document.getElementById('errorContent');
const errorScore = document.getElementById('errorScore');

function popupUpdateForm(createdDate, reviewId, score, content) {
    closeOnlyThisModal(globalSelection);

    let currentDateTime = new Date();
    let createdDateTime = new Date(createdDate);
    let timeDifference = currentDateTime - createdDateTime;
    let sevenDays = 7 * 24 * 60 * 60 * 1000;

    if (timeDifference > sevenDays) {
        showGlobalDialogue("7일 이상이 지난 후기는<br>수정이 불가능 합니다.")

        return;
    }

    reviewIdInput.value = reviewId;
    scoreInput.value = score;
    stars.forEach((star, index) => {
        if (index < score) {
            star.classList.add('on');
        } else {
            star.classList.remove('on');
        }
    });
    contentArea.value = content;
    typeCounter.textContent = content.length;
    mrConfirmBtn.disabled = content.length < 10;

    openModal(modalReviewForm);
}

function setReviewScore(score) {
    stars.forEach(star => {
        star.classList.remove('on')
    })
    for (let i = 0; i < score; i++) {
        stars[i].classList.add('on');
    }
    scoreInput.value = score;
}

function checkTypeCounts() {
    // 작성 완료 버튼 통제
    mrConfirmBtn.disabled = contentArea.value.length < 10

    // 글자 수 통제
    const maxCharCount = 200;
    if (contentArea.value.length > maxCharCount) {
        contentArea.value = contentArea.value.slice(0, maxCharCount);
    }
    typeCounter.textContent = `${contentArea.value.length}`;
}

function recheckReviewForm() {
    const reviewId = reviewIdInput.value;
    const score = scoreInput.value;
    const content = contentArea.value;

    console.log(reviewId)
    console.log(score)
    console.log(content)

    showGlobalSelection("후기를 수정하시겠습니까?",
        () => requestUpdateReview(reviewId, score, content))
}

function requestUpdateReview(reviewId, score, content) {
    closeOnlyThisModal(globalSelection);

    const reviewUpdateDto = {
        reviewId: reviewId,
        score: score,
        content: content
    }

    fetch('/reviews/update', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json', // JSON 형태의 데이터를 전송한다고 명시
        },
        body: JSON.stringify(reviewUpdateDto)
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

                console.log(data)

                vibrateTarget(modalReviewForm);

                data.errors.forEach(error => {
                    if (error.field === 'content' || error.field === 'error') {
                        errorContent.innerHTML = error.message;
                    } else if (error.field === 'score') {
                        errorScore.innerHTML = error.message;
                    }
                })
            })
        });
}

//////////////////////////////////////////////////////////////////

document.querySelectorAll('.reviewUpdateBtn').forEach(reviewUpdate => {

        reviewUpdate.addEventListener('click', function () {
            let createdDate = this.getAttribute('data-created-date');
            let reviewId = this.getAttribute('data-review-id');
            let score = this.getAttribute('data-score');
            let content = this.getAttribute('data-content');

            showGlobalSelection("후기를 수정하시겠습니까?<br>(작성 후 일주일이 지난 후기는<br>수정이 불가능 합니다.)",
                () => popupUpdateForm(createdDate, reviewId, score, content))
        })
    }
)

document.getElementById('mrConfirmBtn').addEventListener('click', function () {
    recheckReviewForm();
})