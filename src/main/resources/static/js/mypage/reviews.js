import {showCustomModal, showGlobalDialogue, showGlobalSelection, closeSingleModal} from "../global-js/global-modal.js";
import {addSlideEvent} from "../global-js/image-slide.js";
import {modalLayouts} from "../layouts/mypage/modalLayouts.js";
import {reviewService} from "../services/mypage/reviewService.js";

addSlideEvent();

const reviewWriteFormModal = document.getElementById('reviewWriteFormModal');
////
// 리뷰 작성 모달
document.querySelectorAll('.reservationWrite').forEach(write => {
    write.addEventListener('click', async () => {
        const index = write.getAttribute('idx');

        reviewWriteFormModal.innerHTML = await modalLayouts.reviewWriteFormModalLayout(contents[index]);

        showCustomModal(reviewWriteFormModal);

        const reviewWriteTxArea = reviewWriteFormModal.querySelector('#reviewWriteTxArea');
        const typeCounter = reviewWriteFormModal.querySelector('#typeCounter');
        const stars = reviewWriteFormModal.querySelectorAll('.mrf-star');
        const reviewScoreInput = reviewWriteFormModal.querySelector('#reviewScoreInput');
        const reviewWriteButton = reviewWriteFormModal.querySelector('#reviewWriteButton');

        reviewWriteTxArea.addEventListener('input', () => {
            const maxCharCount = 200;
            if (reviewWriteTxArea.value.length > maxCharCount) {
                reviewWriteTxArea.value = reviewWriteTxArea.value.slice(0, maxCharCount);
            }
            typeCounter.textContent = `${reviewWriteTxArea.value.length}`;

            reviewWriteButton.disabled = reviewWriteTxArea.value.length < 10;
        });

        stars.forEach(star => {
            star.addEventListener('click', () => {
                const score = star.getAttribute('value');

                stars.forEach(star => {
                    star.classList.remove('on')
                });

                for (let i = 0; i < score; i++) {
                    stars[i].classList.add('on');
                }

                reviewScoreInput.value = score;
            });
        });

        reviewWriteButton.addEventListener('click', () => {
            const msg = "후기를 작성하시겠습니까?<br><br>후기 작성후 삭제가 불가능하나<br>작성일로부터 7일 동안 수정이<br>가능합니다.";
            const reservationId = reviewWriteButton.getAttribute('rId');

            showGlobalSelection(
                msg,
                async () => registerReview(reservationId, reviewScoreInput.value, reviewWriteTxArea.value),
                null,
                false
            );
        });
    });
});

const registerReview = async (reservationId, score, content) => {
    const {success, data, message} = await reviewService.registerReview(reservationId, score, content);

    if (success) {
       alert(message);
       window.location.reload();
    } else {
        closeSingleModal("gs");

        const errorContent = reviewWriteFormModal.querySelector('#errorContent');

        errorContent.innerText = data[0].message;
    }
}

document.querySelectorAll('.reservationDelete').forEach(reservationDelete => {
    reservationDelete.addEventListener('click', function () {
        const reservationId = this.getAttribute('rid');

        showGlobalSelection(
            "리뷰는 호스트에게<br>큰 힘이 됩니다!<br>그래도 삭제하시겠습니까?",
            () => {
                setNotReview(reservationId);
            }
        )
    })
})

const setNotReview = async (reservationId) => {
    const {success, message} = await reviewService.setNotReviewing(reservationId);

    if (success) {
        alert(message);

        window.location.reload();
    } else {
        showGlobalDialogue(message);
    }
}