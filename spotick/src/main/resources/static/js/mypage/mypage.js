const modalBg = document.querySelector('.modal-bg');
const globalDialog = document.querySelector('.global-dialog-container');
const globalSelectionContainer = document.querySelector('.global-selection-container');
const globalSelctionQuestion = document.querySelector('.gs-question');
const globalSelectionConfirm = document.querySelector('.gs-confirm');
const modalReservation = document.querySelector('.modal-reservation-info');
const modalReviewForm = document.querySelector('.modal-review-form-container')

modalBg.addEventListener("click", (e) => {
    if(e.target == modalBg){
        modalBg.classList.remove('show');
        globalDialog?.classList.remove('show');
        globalSelectionContainer?.classList.remove('show');
        modalReservation?.classList.remove('show');
        modalReviewForm?.classList.remove('show');
    }
})

function closeModal() {
    modalBg.classList.remove('show');

    const showElements = document.querySelectorAll('.modal-bg .show');
    showElements.forEach(element => {
        element.classList.remove('show');
    });
}

function closeGlobalSelection(){
    globalSelectionContainer.classList.remove('show')

    if (!(modalReservation?.classList.contains('show') || modalReviewForm?.classList.contains('show'))) {
        modalBg.classList.remove('show');
    }
}

// 예약 정보 상세
const reservationInfos = document.querySelectorAll('.mpcr-info');

reservationInfos.forEach(reservationInfo => {
    reservationInfo.addEventListener('click', () => {
        modalBg.classList.add('show');
        modalReservation.classList.add('show');
    })
})

