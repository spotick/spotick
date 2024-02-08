const modalBg = document.querySelector('.modal-bg');
const globalDialogue = document.querySelector('.global-dialogue-container');
const globalDialogueContent = globalDialogue.querySelector('.gd-content');
const globalDialogueBtn = globalDialogue.querySelector('.gd-btn');

const globalSelection = document.querySelector('.global-selection-container');
const globalSelectionQuestion = globalSelection.querySelector('.gs-question');
const globalSelectionConfirm = globalSelection.querySelector('.gs-confirm');

const modalReservation = document.querySelector('.modal-reservation-info');
const modalReviewForm = document.querySelector('.modal-review-form-container');

const profileModal = document.getElementById('profileModal');
const nicknameModal = document.getElementById('nicknameModal');
const phoneModal = document.getElementById('phoneModal');

const modalPlace = document.querySelector('.modal-place');
const modalTicket = document.querySelector('.modal-ticket');
const modalInquiry = document.querySelector('.modal-inquiry');

modalBg.addEventListener("click", (e) => {
    if (e.target === modalBg) {
        modalBg.classList.remove('show');

        const childElements = modalBg.children;
        for (const child of childElements) {
            child.classList.remove('show');
        }
    }
});

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

function closeOnlyThisModal(target) {
    target.classList.remove('show');
}

function showGlobalDialogue(dialogueString) {
    globalDialogueContent.innerHTML = dialogueString;
    openModal(globalDialogue);
}

function showGlobalSelection(dialogueString, callback) {
    globalSelectionQuestion.innerHTML = dialogueString;
    globalSelectionConfirm.onclick = callback;
    openModal(globalSelection);
}

function closeGlobalSelection() {
    globalSelection.classList.remove('show')

    if (!(modalReservation?.classList.contains('show') || modalReviewForm?.classList.contains('show'))) {
        modalBg.classList.remove('show');
    }
}

function toggleDropdown(button) {
    let dropdown = button.querySelector('.mpc-dropdown');
    dropdown.classList.toggle('show');
}


const topNavigationButtons = document.querySelectorAll('.mpc-top-nav-button');


topNavigationButtons.forEach(button => {
    button.addEventListener('click', (e) => {
        // Remove "active" class from all buttons
        topNavigationButtons.forEach(btn => {
            btn.classList.remove('active');
        });

        // Add "active" class to the clicked button
        button.classList.add('active');
    });
});

function scrollToTop() {
    const duration = 300;
    const start = window.scrollY;
    const target = 0;
    const startTime = performance.now();

    function animateScroll() {
        const currentTime = performance.now();
        const elapsed = currentTime - startTime;

        const easedTime = (elapsed / duration) < 0.5
            ? 2 * Math.pow(elapsed / duration, 2)
            : -1 + (4 - 2 * elapsed / duration) * (elapsed / duration);

        window.scrollTo(0, start + (target - start) * easedTime);

        if (elapsed < duration) {
            requestAnimationFrame(animateScroll);
        }
    }

    animateScroll();
}

const loadingMarkService = (function () {
    const loadingMark = document.getElementById('mpLoadingMark');

    function show() {
        loadingMark.classList.add('show');
    }

    function hide() {
        loadingMark.classList.remove('show')
    }

    return {
        show:show,
        hide:hide
    }
})();

function vibrateTarget(target) {
    target.classList.add('vibration');

    setTimeout(function() {
        target.classList.remove("vibration");
    }, 200);
}

// 날짜를 한국어 방식으로 변경
function formatKoreanDate(dateString) {
    return new Date(dateString).toLocaleString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        hour12: false,
    });
}