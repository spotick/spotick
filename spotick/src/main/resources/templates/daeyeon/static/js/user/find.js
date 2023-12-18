// 아이디/비밀번호 버튼에 따른 각각 찾기 보여주기
$('.title-box').on('click', function() {
  let isId = $(this).text().trim() === '아이디';
  
  $('.id-section').toggleClass('none', !isId);
  $('.password-section').toggleClass('none', isId);

  $('.title-box').removeClass('find-focus');
  $(this).addClass('find-focus');
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
$('.id-section .name').on('change', function() {
  validateInput($(this), /^[가-힣A-Za-z]+$/);
  updateCertButtonState();
});

$('.id-section .tel').on('change', function() {
  validateInput($(this), /^\d{10,11}$/);
  updateCertButtonState();
});

$('.cert-number-label').on('click','.cert-btn.on',function(){
// 아이디찾기 인증번호 전송 및 처리하기
//   $.ajax({
// 		url: '',
// 		type: 'post',
// 		data: { name: $('#name').val(),
// 					tel: $('#tel').val()
// 				},
// 		success: function(result){
// 			console.log('');
// 		} ,
// 		error: (xhr, status, error) => console.log(error),
// 	});

    $('#certNumber').attr('readonly',false);
});







