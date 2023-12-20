// 단순 알림형태의 모달창 컨트롤
const globalDialogModal = document.querySelector('.global-dialog-bg');
const globalDialogContent = document.querySelector('.gd-content');

function closeGDModal() {
    globalDialogModal.classList.remove('show');
    globalDialogContent.innerHTML = "";
}