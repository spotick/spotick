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

function isAllCheck() {
    for (let i = 0; i < $(".check").length; i++) {
        if (!$(".check")[i].classList.contains("on")) {
            return false;
        }
    }
    return true;
}

// 필수 동의목록 체크 확인
function requiredCheckOk() {
    for (let i = 0; i < $(".required").length; i++) {
        if (!$(".required")[i].classList.contains("on")) {
            return false;
        }
    }
    return true;
}

// 인풋 및 체크여부에 따라 서브밋 버튼 동작 설정
$("body").on("blur",'input', function () {
    // 간단한 회원가입시의 체크 검사
    // $(".submit-btn").toggleClass("on", requiredCheckOk());
    // 모든 유효성 검사 적용
    $(".submit-btn").toggleClass("on", isValidFields());
});

// 이벤트 위임으로 회원가입 폼 제출처리
$(".submit-box").on("click", ".on", function () {
    $(".join-wrap").submit();
});

function isValidFields(){
    return isValidEmail()
        &&isValidNickName()
        &&isValidPasswordLength()
        &&isInputEmpty()
        &&requiredCheckOk();
}


// 이메일 유효성 및 중복 검사
function isValidEmail() {
    const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if( regex.test($("#email").val())){
        return false;
    }
    $.ajax({
        url: ``, //url
        type: "post",
        data: $("#email").val(),
        success: function (result) {
            if (!result) {
                //해당 닉네임이 있는지 없는지
                $(".email-fail").removeClass("none");
                return true;
            } else {
                $(".email-fail").addClass("none");
                return false;
            }
        },
        error: function (a, b, c) {
            console.error(c);
        },
    });
}

// 닉네임 중복 검사
function isValidNickName() {
    $.ajax({
        url: ``, //url
        type: "post",
        data: $("#nick-name").val(),
        success: function (result) {
            if (!result) {
                //해당 닉네임이 있는지 없는지 숫자로 가져온다(0,1)
                $(".nick-name-fail").removeClass("none");
                return true;
            } else {
                $(".nick-name-fail").addClass("none");
                return false;
            }
        },
        error: function (a, b, c) {
            console.error(c);
        },
    });
}

// 비밀번호 유효성 검사
function isValidPasswordLength() {
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$/;
    if (passwordRegex.test($("#password").val())) {
        $(".password-fail").removeClass("none");
        return true;
    } else {
        $(".password-fail").addClass("none");
        return false;
    }
}

function isInputEmpty() {
    let isValid = true;
    $("input").each(function () {
        if ($(this).val().trim() === "") {
            isValid = false;
            return false; // 반복 중단
        }
    });
    return isValid;
}

