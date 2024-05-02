import {showGlobalDialogue, showGlobalSelection, showCustomModal, closeSingleModal} from "../global-js/global-modal.js";
import {modalLayouts} from "../layouts/mypage/modalLayouts.js";
import {reviewService} from "../services/mypage/reviewService.js";

const reviewFormModal = document.getElementById('reviewFormModal');

document.querySelectorAll('.reviewUpdateBtn').forEach(reviewUpdate => {
    reviewUpdate.addEventListener('click', () => {
        const index = reviewUpdate.getAttribute('idx');
        const dto = contents[index];

        const now = new Date();
        const createdDateTime = new Date(dto.createdDate);
        const timeDifference = now - createdDateTime;
        const sevenDays = 7 * 24 * 60 * 60 * 1000;

        if (timeDifference > sevenDays) {
            showGlobalDialogue("7일 이상이 지난 후기는<br>수정이 불가능 합니다.");
            return;
        }

        reviewFormModal.innerHTML = modalLayouts.reviewEditFormModalLayout(dto);

        showCustomModal(reviewFormModal);

        const reviewWriteTxArea = reviewFormModal.querySelector('#reviewWriteTxArea');
        const typeCounter = reviewFormModal.querySelector('#typeCounter');
        const stars = reviewFormModal.querySelectorAll('.mrf-star');
        const reviewScoreInput = reviewFormModal.querySelector('#reviewScoreInput');
        const reviewWriteButton = reviewFormModal.querySelector('#reviewWriteButton');

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
            const reviewId = reviewWriteButton.getAttribute('rId');

            showGlobalSelection(
                msg,
                async () => editReview(reviewId, reviewScoreInput.value, reviewWriteTxArea.value),
                null,
                false
            );
        });
    });
});

const editReview = async (reviewId, score, content) => {
    const {success, message} = await reviewService.editReview(reviewId, score, content);

    if (success) {
        alert(message);
        window.location.reload();
    } else {
        closeSingleModal("gs");

        const errorLine = reviewFormModal.querySelector('#errorLine');
        errorLine.innerHTML = message;
    }
}