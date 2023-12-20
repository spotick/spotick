let passwordContent = '';

// 아이디/비밀번호 버튼에 따른 각각 찾기 보여주기
$('.title-box').on('click', function () {
    let isId = $(this).text().trim() === '아이디';
    $('.id-section').toggleClass('none', !isId);
    if(passwordContent !== ''){
        $('.password-wrap').html(passwordContent)
    }
    $('.password-section').toggleClass('none', isId);

    $('.title-box').toggleClass('find-focus');

    $('input').val('');
    $('button').removeClass('on');
    $('.cert-btn').text('인증번호');
    $('.cert-number-label input').attr('readonly', true);

    $('.warning-msg').addClass('none');

});

// 이름과 전화번호 유효성 검사 함수
function validateInput($input, regex) {
    let $warningMsg = $input.closest('.input-box').find('.warning-msg');
    let isOk = regex.test($input.val());
    $warningMsg.toggleClass('none', isOk);
    return isOk; // 유효성 검사 결과를 반환합니다.
}

// 이름과 전화번호 유효성 검사 함수
function validateInput($input, regex) {
    let $warningMsg = $input.closest('.input-box').find('.warning-msg');
    let isOk = regex.test($input.val());
    $warningMsg.toggleClass('none', isOk);
}

// 인증 버튼 상태 업데이트 함수
function updateCertButtonState() {
    let isNameValid = /^[가-힣A-Za-z]+$/.test($('.id-section .name').val().trim());
    let isTelValid = /^\d{10,11}$/.test($('.id-section .tel').val().trim());

    $('.cert-btn').toggleClass('on', isNameValid && isTelValid);
}

// 이벤트 핸들러 설정
$('.id-section .name').on('change', function () {
    validateInput($(this), /^[가-힣A-Za-z]+$/);
    updateCertButtonState();
});

$('.id-section .tel').on('change', function () {
    validateInput($(this), /^\d{10,11}$/);
    updateCertButtonState();
});

$('.cert-number-label,.password-wrap').on('click', '.cert-btn.on', function () {
    // 사용자에게 인증번호를 보내주는 메소드
    // sendFindIdAuthenticationCode();
    $(this).siblings('input').attr('readonly', false);
    $(this).text('재전송');
});

$('#certNumber').on('blur', function () {
    let isTrue = $(this).val() !== '';
    $('.find-id-btn').toggleClass('on', isTrue);
});
$('.password-wrap').on('blur','#passwordCertNumber', function () {
    let isTrue = $(this).val() !== '';
    $('.find-password-btn').toggleClass('on', isTrue);
});



$('.id-section').on('click', '.find-id-btn.on',function (){
//     사용자가 입력한 인증번호가 맞는지 검사하고
//     맞으면 해당 사용자의 아이디를 보여주고 
//     틀리면 다시 인증하게 하기
//     ajax결과 넘겨주기

    showFoundId({email:'qwer1234@ooo.com',registerDate: '2023년 12월 01일'});
});

$('.password-wrap').on('click', '.find-password-btn.on',function (){
    console.log('비밀번호 찾기!!!');
//     사용자가 입력한 인증번호가 맞는지 검사하고
//     맞으면 해당 비밀번호를 변경할 수 있게
//     틀리면 다시 인증하게 하기
    passwordContent = $('.password-wrap').html();
    setPasswordChangeContent();
});

$('.password-wrap').on('blur','#email',function (){
    let regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    let isTrue = regex.test($(this).val())
    let $warningMsg = $(this).closest('.input-box').find('.warning-msg');
    if(isTrue){
        $('.cert-btn').addClass('on');
        $warningMsg.addClass('none')
    }else{
        $('.cert-btn').removeClass('on');
        $warningMsg.removeClass('none')
    }
});

$('.cert-number-label, .password-wrap .cert-number-label input').on('blur', function () {
    let isTrue = $(this).val() !== '';
    $('.find-btn').toggleClass('on', isTrue);
});



// 사용자에게 인증번호를 보내주는 메소드
function sendFindIdAuthenticationCode() {
    // 아이디찾기 인증번호 전송 및 처리하기
    $.ajax({
        url: '',
        type: 'post',
        data: {
            name: $('#name').val(),
            tel: $('#tel').val()
        },
        success: function (result) {
            console.log('');
        },
        error: (xhr, status, error) => console.log(error),
    });
}

function setPasswordChangeContent(){
    let text = `
        <section class="flex-center password-set-section">
            <p class="guide-msg">새로 사용할 비밀번호를 입력해 주세요</p>
            <div class="input-box">
                <label for="password">
                    <input id="password" type="password" class="password" name="password"
                           placeholder="비밀번호(영문+숫자+특수문자,6~15자)">
                </label>
                <p class="invalid-password-msg none">비밀번호는 영문+숫자+특수문자,6~15자로 입력해 주세요</p>
            </div>
            <div class="input-box">
                <label for="passwordCheck" class="password-check-label">
                    <input id="passwordCheck" type="password" class="passwordCheck" name="passwordCheck"
                           placeholder="비밀번호 확인">
                </label>
                <p class="check-fail-msg none">비밀번호가 일치하지 않습니다</p>
            </div>                
            <button type="button" class="change-password-btn flex">비밀번호 변경</button>
        </section>
    `;
    $('.password-wrap').html(text);
}

$('.password-wrap').on('blur','.password-set-section input',function (){
    let isValidPassword = passwordValidation();
    let isSamePassword = checkPasswordMatching();
    let isTrue = isValidPassword&& isSamePassword;

    $('.invalid-password-msg').toggleClass('none',isValidPassword);
    $('.check-fail-msg').toggleClass('none',isSamePassword);
    $('.change-password-btn').toggleClass('on',isTrue);
});

// 비밀번호 변경 처리
$('.password-wrap').on('click','.change-password-btn.on',function (){
    console.log('비밀번호 변경!!!')
    alert('비밀번호 변경!!')
});


// 비밀번호 유효성 검사
function passwordValidation(){
    let regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,15}$/;
    return regex.test($('#password').val());
}

function checkPasswordMatching(){
    return $('#password').val() === $('#passwordCheck').val();
}

function showFoundId(result){
    let text = `
        <div class="guide-msg flex-align-center">
          <p>아이디 찾기가 완료되었습니다.</p>
        </div>
        <div class="find-completed-container">
            <section class="id-completed flex-center">
                <div class="find-id-info-box">
                    <h4>${result.email}</h4>
                    <div class="register-date">
                        가입일: ${result.registerDate}
                    </div>
                </div>
                <div class="flex-align-center">
                    <p class="find-guide-msg">
                        아이디/비밀번호 찾기가 안되는 경우,
                        고객센터 02-0000-0000로 문의 부탁드립니다.
                        문의 시간은 평일 : 10:00~19:00입니다.
                    </p>
                </div>
                <a href="#" class="go-login flex-align-center">로그인</a>
                </section>
        </div>
    `;

    $('.find-wrap').html(text);
}












