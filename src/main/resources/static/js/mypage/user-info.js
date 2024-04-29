// 비밀번호 수정관련
const pwChangeConfirmBtn = document.getElementById('pwChangeConfirm');

function isValidPassword(password) {
    // 최소 6자, 최대 15자, 영문, 숫자, 특수문자 2가지 이상 포함
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,15}$/;

    return passwordRegex.test(password);
}

function confirmChangingPassword() {
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

    let newPassword = document.getElementById("newPw").value;
    let newPasswordCheck = document.getElementById("newPwCheck").value;

    let form = document.getElementById("changePasswordForm");
    form.querySelector('[name="password"]').value = newPassword;
    form.querySelector('[name="passwordCheck"]').value = newPasswordCheck;

    form.submit();
}