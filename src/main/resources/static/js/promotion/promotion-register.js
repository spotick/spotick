let $fileList = $('.file-wrap');
let width = $('.file-container').width();
let idx = 0;
let length = 0;

$('#file').on('change', function (event) {
    let files = Array.from(event.target.files);

    // 미리보기 이미지 목록을 비웁니다.
    $('.file-wrap').empty();

    files.forEach((file, i) => {
        let reader = new FileReader();

        reader.onload = function (e) {
            let li = `
                <li class="file-item">
                    <img src="${e.target.result}" alt="">
                    <button type="button" class="delete" data-name="${file.name}">
                        <i class="fa-solid fa-x"></i>
                    </button>
                </li>
            `;
            $('.file-wrap').append(li);
        };
        reader.readAsDataURL(file);
    });
    updatePreviewVisibility(true);
    length = files.length;
    checkEnd();
});

// 미리보기 사진 다음버튼
$('.next').on('click', function () {
    idx++;
    $fileList.css('transform', 'translateX(' + (-width * idx) + 'px)').css('transition', '0.5s');
    checkEnd();
});

// 미리보기 사진 이전버튼
$('.prev').on('click', function () {
    idx--;
    $fileList.css('transform', 'translateX(' + (-width * idx) + 'px)').css('transition', '0.5s');
    checkEnd();
});

$('.file-wrap').on('click', '.delete', function () {
    let files = $('#file')[0].files;

    // 삭제된 이미지의 인덱스를 찾습니다.
    let deletedIndex = $(this).parent().index();

    $(this).parent().remove();
    let fileName = $(this).data('name');
    let dt = new DataTransfer();

    for (let i = 0; i < files.length; i++) {
        if (files[i].name !== fileName) {
            dt.items.add(files[i]);
        }
    }

    files = dt.files;
    $('#file')[0].files = files;

    length = $('.file-item').length;

    // 삭제된 이미지가 현재 인덱스보다 앞에 있으면 idx를 감소시킵니다.
    if (deletedIndex < idx) {
        idx--;
    }

    // 삭제 후 현재 인덱스가 목록 길이를 초과하지 않도록 조정합니다.
    if (idx >= length) {
        idx = length - 1;
    }

    if (length === 0) {
        updatePreviewVisibility(false);
        idx = 0; // 목록이 비었을 때 idx를 0으로 리셋합니다.
    }

    // translateX 값을 업데이트합니다.
    $fileList.css('transform', 'translateX(' + (-width * idx) + 'px)').css('transition', '0.5s');

    checkEnd();
});


function checkEnd() {
    if (idx <= 0) {
        $('.prev').hide();
    } else {
        $('.prev').show();
    }
    if (idx >= length - 1) {
        $('.next').hide();
    } else {
        $('.next').show();
    }
}

function updatePreviewVisibility(isTrue) {
    $('.file-label').toggleClass('none', isTrue);
    $('.file-container').toggleClass('none', !isTrue);
}



$(document).ready(function() {
    $('#summernote').summernote({
        height: 500,
        minHeight: null,
        maxHeight: null,
        lang: "ko-KR",
        resize: false,
        callbacks: {
            onImageUpload: function (files, editor, welEditable) {
                // 파일 업로드 (다중 업로드를 위해 반복문 사용)
                for (var i = files.length - 1; i >= 0; i--) {
                    var fileName = files[i].name

                    // 이미지 alt 속성 삽일을 위한 설정
                    var caption = prompt('이미지 설명 :', fileName)
                    if (caption === '') {
                        caption = '이미지'
                    }
                    uploadSummernoteImg(files[i], this, caption)
                }
            },
        }
    });
});

function uploadSummernoteImg(file, el, caption) {
    const data = new FormData();
    data.append('uploadFile', file)
    $.ajax({
        data: data,
        type: 'POST',
        url: '/file/summernote/upload',
        contentType: false,
        enctype: 'multipart/form-data',
        processData: false,
        success: function (data) {
            console.log(data);

            // /file/display?(link);
            // $(el).summernote(
            //     'editor.insertImage',
            //     data.url,
            //     function ($image) {
            //         $image.attr('alt', caption);
            //     }
            // )
        },
    })
}