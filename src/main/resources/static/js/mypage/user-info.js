import {showGlobalDialogue, showCustomModal, closeEveryModal} from "../global-js/global-modal.js";
import {userService} from "../services/user/userService.js";

const userImage = document.getElementById('userImage');
const profileModal = document.getElementById('profileModal');

// 프로필 이미지
userImage.addEventListener('click', () => {
    showCustomModal(profileModal);
});

const defaultImages = document.querySelectorAll('.def-pro');
const userProfileImage = document.getElementById('userProfileImage');
const successUser = document.getElementById('successUser');
const errorUser = document.getElementById('errorUser');

defaultImages.forEach(image => {
    image.addEventListener('click', async () => {
        const imageName = image.getAttribute('img');

        const {success, message} = await userService.updateDefaultImage(imageName);


        if (success) {
            successUser.innerHTML = message;

            userProfileImage.src = `/file/default/display?fileName=${imageName}`;
        } else {
            errorUser.innerHTML = message;
        }

        closeEveryModal();
    });
});

const imageUploadInput = document.getElementById('imageUploadInput');
imageUploadInput.addEventListener('change', async (e) => {
    const file = e.target.files[0];

    const r = checkFileSize(file);

    if (!r) {
        return;
    }

    const {success, message, data} = await userService.updatePersonalImage(file);

    if (success) {
        const {uploadPath, uuid, fileName} = data;
        successUser.innerHTML = message;

        userProfileImage.src = `/file/display?fileName=${uploadPath}/${uuid}_${fileName}`;
    } else {
        errorUser.innerHTML = message;
    }

    closeEveryModal();
});

function checkFileSize(file) {
    // 파일 크기를 확인하고 1MB를 초과하는 경우
    if (file && file.size > 1048576) {
        showGlobalDialogue("파일 크기가 1MB를 초과합니다.<br>더 작은 파일을 선택해주세요.")
        return false;
    }

    return true;
}

// 닉네임
const userNickname = document.getElementById('userNickname');
const nicknameModal = document.getElementById('nicknameModal');
const newNicknameInput = document.getElementById('newNicknameInput');
const nicknameChangeButton = document.getElementById('nicknameChangeButton');

const currentNickname = document.getElementById('curNickname');

userNickname.addEventListener('click', () => {
    showCustomModal(nicknameModal);
    newNicknameInput.value = ``;
    nicknameChangeButton.disabled = true;
    newNicknameInput.focus();
});

newNicknameInput.addEventListener('keyup', (e) => {
    checkNickname();
    if (e.key === 'Enter' && newNicknameInput.value.length >= 2) {
        changeNickname();
    }
});

nicknameChangeButton.addEventListener('click', changeNickname);

function checkNickname() {
    nicknameChangeButton.disabled = newNicknameInput.value.length < 2;
}

async function changeNickname() {
    const {success, message, data} = await userService.updateNickname(newNicknameInput.value);

    if (success) {
        successUser.innerHTML = message;

        currentNickname.innerHTML = data;
    } else {
        errorUser.innerHTML = message;
    }

    closeEveryModal();
}

// 전화번호
const telButton = document.getElementById('telButton');
const telModal = document.getElementById('telModal');

const newTelInput = document.getElementById('newTelInput');
const newTelInputEraseBtn = document.getElementById('newTelInputEraseBtn');
const requestAuthCodeButton = document.getElementById('requestAuthCodeButton');

const authNumInput = document.getElementById('authNumInput');
const timerText = document.getElementById('timerText');
const authConfirmButton = document.getElementById('authConfirmButton');

const errorAuth = document.getElementById('errorAuth');

const successPhone = document.getElementById('successPhone');
const currentTel = document.getElementById('curTel');

telButton.addEventListener('click', () => {
    resetTelModal();
    showCustomModal(telModal);
    newTelInput.focus();
});

newTelInput.addEventListener('input', () => {
    newTelInput.value === ""
        ? newTelInputEraseBtn.classList.remove('show')
        : newTelInputEraseBtn.classList.add('show');

    requestAuthCodeButton.disabled = !/^010\d{8}$/.test(newTelInput.value);
});

newTelInputEraseBtn.addEventListener('click', () => {
    newTelInput.value = ``;
    newTelInputEraseBtn.classList.remove('show');
    requestAuthCodeButton.disabled = true;
    newTelInput.focus();
});

requestAuthCodeButton.addEventListener('click', async () => {
    const r = await userService.startTelVerification(newTelInput.value);

    if (!r) {
        console.error('오류 발생');
        return;
    }

    newTelInput.parentElement.classList.add('disable');
    authNumInput.parentElement.classList.remove('disable');
    newTelInputEraseBtn.classList.remove('show');
    requestAuthCodeButton.disabled = true;
    authNumInput.focus();

    startTimer();
});

authNumInput.addEventListener("input", () => {
    let authNumValue = authNumInput.value;

    authConfirmButton.disabled = authNumValue.length !== 6;
});

authConfirmButton.addEventListener('click', async () => {
    const {success, message, data} = await userService.updateTel(newTelInput.value, authNumInput.value);

    if (success) {
        stopTimer();

        currentTel.value = data
        successPhone.innerHTML = message;

        closeEveryModal(resetTelModal);
    } else {
        errorAuth.innerHTML = message;
    }
})


function resetTelModal() {
    newTelInput.value = '';
    newTelInputEraseBtn.classList.remove('show');
    requestAuthCodeButton.disabled = true;
    newTelInput.parentElement.classList.remove('disable');
    authNumInput.value = '';
    authNumInput.parentElement.classList.add('disable');
    stopTimer();
    authConfirmButton.disabled = true;
    errorAuth.innerHTML = '';
}

let verificationTimer;
const TIME_DURATION = 60; // 디폴트 타이머 시간

function startTimer() {
    let timer = TIME_DURATION; // 작동 타이머

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
    stopTimer();
    errorAuth.innerHTML = "입력시간이 초과되었습니다. 다시 시도해주세요."
    authNumInput.parentElement.classList.add('disable');

    newTelInput.parentElement.classList.remove('disable');
    authNumInput.value = '';

    authConfirmButton.disabled = true;
    errorAuth.innerHTML = '';
}


// 패스워드
const newPw = document.getElementById('newPw');
const newPwCheck = document.getElementById('newPwCheck');

const successPw = document.getElementById('successPw');
const errorPw = document.getElementById('errorPw');

const pwChangeConfirm = document.getElementById('pwChangeConfirm');

pwChangeConfirm.addEventListener('click', async () => {
    let password = newPw.value;
    let passwordCheck = newPwCheck.value;

    // 수정하고자 하는 비밀번호가 조건에 맞지 않을 시(혹은 없을 시)
    if (!isValidPassword(password)) {
        successPw.innerHTML = "";
        errorPw.innerHTML = "비밀번호를 확인해주세요.";
        return;
    }
    // 비밀번호 확인이 일치하지 않을 경우
    if (password !== passwordCheck) {
        successPw.innerHTML = "";
        errorPw.innerHTML = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
        return;
    }

    const {success, message} = await userService.updatePassword(password);

    if (success) {
        successPw.innerHTML = message;
        errorPw.innerHTML = "";
        newPw.value = "";
        newPwCheck.value = "";
    } else {
        successPw.innerHTML = "";
        errorPw.innerHTML = message;
    }
});


function isValidPassword(password) {
    // 최소 6자, 최대 15자, 영문, 숫자, 특수문자 2가지 이상 포함
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,15}$/;

    return passwordRegex.test(password);
}