// 개인정보 수정 =========================================================

// 전화번호 수정
const newPhoneNumCon = document.getElementById('newPhoneNumCon');
const newPhoneInput = document.getElementById('newPhone');
const newPhoneEraseBtn = document.getElementById('newPhoneEraseBtn');
const newPhoneAuthBtn = document.getElementById('newPhoneAuthBtn');
const authNumInputCon = document.getElementById('authNumInputCon');
const authNumInput = document.getElementById('authNumInput');
const modalAlertText = document.querySelector('.modal-alert-text');
const confirmBtn = document.querySelector('.modal-confirm-btn');

function openPhoneModal(modalPhone) {
    newPhoneInput.value = "";
    newPhoneEraseBtn.classList.remove('show');
    newPhoneAuthBtn.classList.add('disable');
    authNumInputCon.classList.add('disable');
    authNumInput.value = "";
    modalAlertText.innerHTML = "";
    confirmBtn.removeAttribute("disabled");
    stopTimer();
    openModal(modalPhone)
}

newPhoneInput.addEventListener("input", () => {
    // 삭제버튼 on
    if (newPhoneInput.value.trim() !== "") {
        newPhoneEraseBtn.classList.add('show');
    } else {
        newPhoneEraseBtn.classList.remove('show');
    }
    // 010으로 시작하고 전화번호 11자리 다 채울시 인증번호 받기 버튼 활성화
    if (/^010\d{8}$/.test(newPhoneInput.value)) {
        newPhoneAuthBtn.classList.remove('disable');
    } else {
        newPhoneAuthBtn.classList.add('disable');
    }
})

// 삭제버튼 누를시 인증번호 disable 및 input값 삭제
newPhoneEraseBtn.addEventListener("click", () => {
    newPhoneEraseBtn.classList.remove('show')
    newPhoneAuthBtn.classList.add('disable');
    newPhoneInput.value = "";
})

// 인증번호 관련
newPhoneAuthBtn.addEventListener("click", () => {
    if (!newPhoneAuthBtn.classList.contains('disable')) {
        authNumInputCon.classList.remove('disable');
        newPhoneNumCon.classList.add('disable');
        newPhoneEraseBtn.classList.remove('show');
        modalAlertText.innerHTML = '';
        startVerification();
    }
})

authNumInput.addEventListener("input", () => {
    let authNumValue = authNumInput.value;

    if (authNumValue.length === 6){
        confirmBtn.removeAttribute('disabled');
    } else {
        confirmBtn.setAttribute('disabled', "true");
    }
})

function startVerification() {
    // 인증번호 발송 로직 필요(서버)

    newPhoneAuthBtn.classList.add('disable');

    startTimer();
}

// 인증번호 전송 로직
function authenticate() {}

let verificationTimer;
const timerDuration = 10; // 디폴트 타이머 시간
const timerContainer = document.getElementById('timerContainer');
const timerText = document.getElementById('timerText');

function startTimer() {
    let timer = timerDuration; // 작동 타이머

    function updateTimer() {
        timer--;

        let minutes = Math.floor(timer / 60);
        let seconds = timer % 60;

        timerText.textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;

        if (timer < 0) {
            handleTimerExpiration();
        }
    }

    updateTimer();

    verificationTimer = setInterval(updateTimer, 1000);
}

function stopTimer() {
    clearInterval(verificationTimer);
    timerText.innerHTML = '';
}

function handleTimerExpiration() {
    modalAlertText.innerHTML = "입력시간이 초과되었습니다. 다시 시도해주세요."
    confirmBtn.classList.add('disable')
    stopTimer();
    newPhoneNumCon.classList.remove('disable');
    newPhoneAuthBtn.classList.remove('disable');
    authNumInput.value = '';
    authNumInputCon.classList.add('disable');
}


// 비밀번호 수정관련
const pwChangeConfirmBtn = document.getElementById('pwChangeConfirm');

function isValidPassword(password) {
    // 최소 6자, 최대 15자, 영문, 숫자, 특수문자 2가지 이상 포함
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,15}$/;

    return passwordRegex.test(password);
}

function confirmChangingPassword(){
    let password = document.getElementById('newPw').value;
    let passwordCheck = document.getElementById('newPwCheck').value;

    // 수정하고자 하는 비밀번호가 조건에 맞지 않을 시(혹은 없을 시)
    if (!isValidPassword(password)) {
        showGlobalDialogue("비밀번호를 확인해주세요.")
        return;
    }
    // 비밀번호 확인이 일치하지 않을 경우
    if (password !== passwordCheck) {
        showGlobalDialogue("비밀번호와 비밀번호 확인이<br>일치하지 않습니다.")
        return;
    }


}