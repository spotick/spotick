$('input,textarea').on('focus', function () {
    $(this).closest('.input-box').css('border-color', '#007AFF');
}).on('blur', function () {
    $(this).closest('.input-box').css('border-color', '#dfe2e7');
});

$('.start').on('click', '.submit-btn.on', function () {
    $(".register-form").submit();
});

$('#placeFile').on('change', function (event) {
    let files = Array.from(event.target.files);

    updatePreviewVisibility();
    updateButtonVisibility(); // 버튼 가시성 업데이트 추가

    // 미리보기 이미지 목록을 비웁니다.
    $('.file-wrap').empty();

    files.forEach((f) => {
        let reader = new FileReader();
        reader.onload = function (e) {
            let li = `
                <li class="file-item">
                    <img src="${e.target.result}" alt="">
                    <button type="button" class="delete" data-fileIdx="">
                        <i class="fa-solid fa-x"></i>
                    </button>
                </li>
            `;
            $('.file-wrap').append(li);
        };
        reader.readAsDataURL(f);
    });
});

// 삭제 버튼 기능
$('.file-wrap').on('click', '.delete', function() {
    $(this).closest('.file-item').remove();
    updatePreviewVisibility();
    updateButtonVisibility(); // 버튼 가시성 업데이트 추가
});

// 이전 버튼 기능
$('.prev').on('click', function() {
    let first = $('.file-wrap .file-item:first');
    $('.file-wrap').append(first);
    updateButtonVisibility(); // 버튼 가시성 업데이트 추가
});

// 다음 버튼 기능
$('.next').on('click', function() {
    let last = $('.file-wrap .file-item:last');
    $('.file-wrap').prepend(last);
    updateButtonVisibility(); // 버튼 가시성 업데이트 추가
});

function updatePreviewVisibility() {
    let isEmpty = $('.file-item').length === 0;
    $('.file-label').toggleClass('none', !isEmpty);
    $('.file-container').toggleClass('none', isEmpty);
}

// 새로운 함수: 버튼 가시성 업데이트
function updateButtonVisibility() {
    let totalItems = $('.file-item').length;
    let firstItemVisible = $('.file-wrap .file-item:first').is(':visible');
    let lastItemVisible = $('.file-wrap .file-item:last').is(':visible');

    $('.prev').toggle(!firstItemVisible);
    $('.next').toggle(!lastItemVisible);
}












































