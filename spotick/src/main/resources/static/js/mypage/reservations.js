// 액션 버튼 컨트롤
const actionBtns = document.querySelectorAll('.mpcr-action-btn');

actionBtns.forEach(actionBtn => {
    actionBtn.addEventListener('click', () => {
        const dropdown = actionBtn.querySelector('.mpcr-action-dropdown');

        dropdown.classList.toggle('show');
    });
});



function showGSForCancelingReservation(placeId) {
    showGlobalSelction("예약을 취소하시겠습니까?", () => cancelReservation(placeId))
}

function cancelReservation(placeId) {
    console.log(placeId);
}

function showGSForRemoveHistory(placeId) {
    showGlobalSelction("예약내역을<br>삭제하시겠습니까?", () => removeHistory(placeId))
}

function removeHistory(placeId) {
    console.log(placeId);
}