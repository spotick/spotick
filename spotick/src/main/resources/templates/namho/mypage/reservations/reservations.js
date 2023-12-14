// 액션 버튼 컨트롤
const actionBtns = document.querySelectorAll('.mpcr-action-btn');

actionBtns.forEach(actionBtn => {
    actionBtn.addEventListener('click', () => {
        const dropdown = actionBtn.querySelector('.mpcr-action-dropdown');

        dropdown.classList.toggle('show');
    });
});