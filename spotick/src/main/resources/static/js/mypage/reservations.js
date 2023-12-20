// 액션 버튼 컨트롤
const actionBtns = document.querySelectorAll('.mpcr-action-btn');

actionBtns.forEach(actionBtn => {
    actionBtn.addEventListener('click', () => {
        const dropdown = actionBtn.querySelector('.mpcr-action-dropdown');

        dropdown.classList.toggle('show');
    });
});


function popupCancelReservation(){
    modalBg.classList.add('show');
    globalSelectionContainer.classList.add('show');
    document.querySelector('.gs-question').innerHTML = "예약을<br>취소하시겠습니까?"
    document.querySelector('.gs-confirm').onclick = () => cancelReservation(2);
}

function cancelReservation(index) {
    console.log(index);
}

function popupRemoveHistory(){
    modalBg.classList.add('show');
    globalSelectionContainer.classList.add('show');
    document.querySelector('.gs-question').innerHTML = "예약내역을<br>삭제하시겠습니까?"
    document.querySelector('.gs-confirm').onclick = () => removeHistory(2);
}

function removeHistory(index) {
    console.log(index);
}