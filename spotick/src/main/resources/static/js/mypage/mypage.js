const modalBg = document.querySelector('.modal-bg');
const globalDialogue = document.querySelector('.global-dialogue-container');
const globalDialogueContent = globalDialogue.querySelector('.gd-content');
const globalDialogueBtn = globalDialogue.querySelector('.gd-btn');

const globalSelection = document.querySelector('.global-selection-container');
const globalSelectionQuestion = globalSelection.querySelector('.gs-question');
const globalSelectionConfirm = globalSelection.querySelector('.gs-confirm');

const modalReservation = document.querySelector('.modal-reservation-info');
const modalReviewForm = document.querySelector('.modal-review-form-container');
const modalPhone = document.querySelector('.modal-phone-container');

const modalPlace = document.querySelector('.modal-place');

modalBg.addEventListener("click", (e) => {
    if(e.target === modalBg){
        modalBg.classList.remove('show');
        globalDialogue?.classList.remove('show');
        globalSelection?.classList.remove('show');
        modalReservation?.classList.remove('show');
        modalReviewForm?.classList.remove('show');
    }
})

// 모달타입을 전달하여 특정 모달창 on
function openModal(modalType) {
    modalBg.classList.add('show');
    modalType.classList.add('show');
}

// 백그라운드 클릭시 모든 모달창 show클래스 제거
function closeModal() {
    modalBg.classList.remove('show');

    const showElements = document.querySelectorAll('.modal-bg .show');
    showElements.forEach(element => {
        element.classList.remove('show');
    });
}

function showGlobalDialogue(dialogueString) {
    globalDialogueContent.innerHTML = dialogueString;
    openModal(globalDialogue);
}

function showGlobalSelction(dialogueString, callback) {
    globalSelectionQuestion.innerHTML = dialogueString;
    globalSelectionConfirm.onclick = callback;
    openModal(globalSelection);
}

function closeGlobalSelection(){
    globalSelection.classList.remove('show')

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

function toggleDropdown(button) {
    let dropdown = button.querySelector('.mpc-dropdown');
    console.log(dropdown);
    dropdown.classList.toggle('show');
}
