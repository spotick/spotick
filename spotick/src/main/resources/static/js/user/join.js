
$(".all-check").on("click", function () {
    $(this).toggleClass("on");

    if ($(this).hasClass("on")) {
        $(".check").addClass("on");
    } else {
        $(".check").removeClass("on");
    }
});

$(".check").on("click", function () {
    $(this).toggleClass("on");

    if (isAllCheck()) {
        $(".all-check").addClass("on");
    } else {
        $(".all-check").removeClass("on");
    }
});

$('input').on('focus',function (){
    $(this).closest('.input-box').css('border-color','#007AFF');
}).on('blur',function (){
    $(this).closest('.input-box').css('border-color','#dfe2e7');
})

function isAllCheck() {
    for (let i = 0; i < $(".check").length; i++) {
        if (!$(".check")[i].classList.contains("on")) {
            return false;
        }
    }
    return true;
}

// 인풋 및 체크여부에 따라 서브밋 버튼 동작 설정
$('.agreement>div').on('click',function (){
    $(".submit-btn").toggleClass("on", isValidFields());
});

$("input").on('change',function (){
    $(".submit-btn").toggleClass("on", isValidFields());
});

// 이벤트 위임으로 회원가입 폼 제출처리
$(".submit-box").on("click", ".on", function () {
    $(".join-wrap").submit();
});

function isValidFields(){
    return
    isInputOk() &&
        requiredCheckOk();
}


// 이메일 유효성 및 중복 검사
function isValidEmail() {
    const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    const $email = $("#email");
    let isValid = regex.test($email.val());
    let $emailFail = $('.email-fail');

    $emailFail.text(isValid?'':'유효하지 않은 이메일 형식입니다.');

    $email.toggleClass('ok',isValid);

    // $.ajax({
    //     url: ``, //url
    //     type: "post",
    //     data: $("#email").val(),
    //     success: function (result) {
    //         if (!result) {
    //             //해당 닉네임이 있는지 없는지
    //             $(".email-fail").removeClass("none");
    //             return true;
    //         } else {
    //             $(".email-fail").addClass("none");
    //             return false;
    //         }
    //     },
    //     error: function (a, b, c) {
    //         console.error(c);
    //     },
    // });
}

// 닉네임 중복 검사
function isValidNickName() {
    let $nickName =$("#nick-name");
    let isValid = $nickName.val() !== '';
    
    $('.nick-name-fail').text(isValid?'':'닉네임을 다시 입력해주세요');

    $nickName.toggleClass('ok',isValid);
    // $.ajax({
    //     url: ``, //url
    //     type: "post",
    //     data: {nickName:$("#nick-name").val()},
    //     success: function (result) {
    //         if (!result) {
    //             //해당 닉네임이 있는지 없는지 숫자로 가져온다(0,1)
    //             $(".nick-name-fail").removeClass("none");
    //             return true;
    //         } else {
    //             $(".nick-name-fail").addClass("none");
    //             return false;
    //         }
    //     },
    //     error: function (a, b, c) {
    //         console.error(c);
    //     },
    // });
}

// 비밀번호 유효성 검사
function isValidPassword() {
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$/;
    let $password = $("#password");
    let isValid = passwordRegex.test($password.val());
    $(".password-fail").toggleClass('none',isValid);
    $password.toggleClass('ok',isValid);

}

function isValidTelNumber(){
    const telRegex = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
    let $tel = $('#tel');
    let isValid = telRegex.test($tel.val());
    $('.tel-fail').text(isValid?'':'올바른 전화번호를 입력해주세요');

    $tel.toggleClass('ok',isValid);
}

function isInputOk(){
    let $inputArr = $('input');

    for (let i = 0; i < $inputArr.length; i++) {
        if (!$inputArr[i].classList.contains("ok")) {
            return false;
        }
    }
    return true;
}

// 필수 동의목록 체크 확인
function requiredCheckOk() {
    for (let i = 0; i < $(`.required`).length; i++) {
        if (!$(".required")[i].classList.contains("on")) {
            return false;
        }
    }
    return true;
}














