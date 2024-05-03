import {showCustomModal, showGlobalDialogue, showGlobalSelection, closeSingleModal} from "../global-js/global-modal.js";
import {addSlideEvent} from "../global-js/image-slide.js";
import {modalLayouts} from "../layouts/mypage/modalLayouts.js";
import {reviewService} from "../services/review/reviewService.js";
import {bookmarkFetch} from "../modules/fetch/bookmarkFetch.js";

addSlideEvent();

const reviewFormModal = document.getElementById('reviewFormModal');
////
// 리뷰 작성 모달
document.querySelectorAll('.reservationWrite').forEach(write => {
    write.addEventListener('click', async () => {
        const index = write.getAttribute('idx');

        reviewFormModal.innerHTML = await modalLayouts.reviewWriteFormModalLayout(contents[index]);

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

        const errorContent = reviewFormModal.querySelector('#errorContent');

        errorContent.innerText = data[0].message;
    }
}

// 리뷰 작성 안함
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

////
// 북마크 기능
const bookmarkButtons = document.querySelectorAll('.ItemBookMarkBtn');

bookmarkButtons.forEach(btn => {

    btn.addEventListener('click', () => {
        toggleBookmark(btn);
    });
})

function toggleBookmark(btn) {
    const placeId = btn.getAttribute('data-id');
    const status = btn.getAttribute('data-status');

    bookmarkFetch(status, placeId)
        .then(boo => {
            btn.setAttribute('data-status', boo);
            const off = btn.children[0];
            const on = btn.children[1];

            if (boo) {
                off.classList.add('none');
                on.classList.remove('none')
            } else {
                off.classList.remove('none');
                on.classList.add('none')
            }
        });
}