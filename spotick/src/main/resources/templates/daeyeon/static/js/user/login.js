// 로그인 유효성검사(이메일 형식, 비밀번호 길이)

// 모든 기능 완성되면 삭제
$('#submit').addClass('on');

// 작업 빨리 완성해야할 때는 주석처리
// $('input').on('change',function(){
//   let email = $('#email').val();
//   let password = $('#password').val();
//   $('#submit').toggleClass('on', isValid(email, password));
// });

// 이벤트 위임으로 로그인 폼 제출처리
$('.submit-box').on('click','.on',function(){
    $('.login-form').submit();
});

// 인풋 포커스되면 간단한 이벤트처리
$('input').on('focus', function() {
    $(this).closest('.input_container').addClass('focused');
}).on('blur', function() {
    $(this).closest('.input_container').removeClass('focused');
});


// 유효성 검사를 묶어주는 메소드
function isValid(email,password){
    return isValidEmailFormat(email)&&isValidPasswordLength(password);
}

// 이메일 유효성 검사
function isValidEmailFormat(email){
    const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return regex.test(email);
}
// 비밀번호 유효성 검사
function isValidPasswordLength(password){
    return password.length>=8;
}












