$('input,textarea').on('focus', function () {
    $(this).closest('.input-box').css('border-color', '#007AFF');
}).on('blur', function () {
    $(this).closest('.input-box').css('border-color', '#dfe2e7');
});

$('.start').on('click', '.submit-btn.on', function () {
    console.log("눌림")
    $(".register-form").submit();
});

let $fileList = $('.file-wrap');
let width = $('.file-container').width();
let idx = 0;
let length = 0;

$('#placeFile').on('change', function (event) {
    let files = Array.from(event.target.files);

    // 미리보기 이미지 목록을 비웁니다.
    $('.file-wrap').empty();

    files.forEach((file,i) => {
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
$('.next').on('click', function() {
    idx++;
    $fileList.css('transform', 'translateX(' + (-width * idx) + 'px)').css('transition', '0.5s');
    checkEnd();
});

// 미리보기 사진 이전버튼
$('.prev').on('click', function() {
    idx--;
    $fileList.css('transform', 'translateX(' + (-width * idx) + 'px)').css('transition', '0.5s');
    checkEnd();
});

$('.file-wrap').on('click','.delete',function (){
    let files = $('#placeFile')[0].files;

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
    $('#placeFile')[0].files = files;

    length = $('.file-item').length;

    // 삭제된 이미지가 현재 인덱스보다 앞에 있으면 idx를 감소시킵니다.
    if(deletedIndex < idx) {
        idx--;
    }

    // 삭제 후 현재 인덱스가 목록 길이를 초과하지 않도록 조정합니다.
    if(idx >= length) {
        idx = length - 1;
    }

    if(length == 0){
        updatePreviewVisibility(false);
        idx = 0; // 목록이 비었을 때 idx를 0으로 리셋합니다.
    }

    // translateX 값을 업데이트합니다.
    $fileList.css('transform', 'translateX(' + (-width * idx) + 'px)').css('transition', '0.5s');

    checkEnd();
});


function checkEnd() {
    if(idx <= 0){
        $('.prev').hide();
    }else {
        $('.prev').show();
    }
    if(idx >= length - 1){
        $('.next').hide();
    }else {
        $('.next').show();
    }
}

function updatePreviewVisibility(isTrue) {
    $('.file-label').toggleClass('none', isTrue);
    $('.file-container').toggleClass('none', !isTrue);
}

// function checkInputs() {
//     const allInputs = $('.price-input-box .placeDefaultPrice');
//     const isAllInputsFilled = allInputs.toArray().every(input => $(input).val().trim() !== '');
//
//     $('.price-add-button').prop('disabled', !isAllInputsFilled);
// }
//
// $('.placeDefaultPrice').blur(function (){
//     checkInputs()
// })

// $(document).ready(function() {
//     $(".price-add-button").on("click", function() {
//         // 값을 가져와서 새로운 티켓을 만듭니다.
//         var ticketClass = $(".ticket-class").val();
//         var ticketPrice = $(".ticket-price").val();
//         var ticketHeadcount = $(".ticket-headcount").val();
//
//         // 값이 비어있지 않은 경우에만 추가합니다.
//         if (ticketClass && ticketPrice && ticketHeadcount) {
//             var newTicket = `
//                 <div class="headcount-input-box">
//                     <div class="input-box">
//                         <input class="placeDefaultPrice ticket-class" type="text" value="${ticketClass}" readonly>
//                     </div>
//                     <button type="button" class="delete-button">x</button>
//                     <div class="input-box">
//                         <input class="placeDefaultPrice ticket-price" type="number" value="${ticketPrice}" readonly>
//                         <p class="unit">원</p>
//                     </div>
//                     <div class="input-box">
//                         <input class="placeDefaultPrice ticket-headcount" type="number" value="${ticketHeadcount}" readonly>
//                         <p class="unit">명</p>
//                     </div>
//                 </div>
//             `;
//
//             $(".headcount-input-box-none").removeClass("On"); // 등록된 티켓이 있으므로 클래스를 제거합니다.
//             $(".headcount-input-box-container").append(newTicket);
//
//             // 추가 후에 입력값 초기화
//             $(".price-input-container .ticket-class").val("");
//             $(".price-input-container .ticket-price").val("");
//             $(".price-input-container .ticket-headcount").val("");
//         }
//     });
//
//     // 동적으로 생성된 요소에 대한 이벤트 처리
//     $(".headcount-input-box-container").on("click", ".delete-button", function() {
//         $(this).closest(".headcount-input-box").remove();
//
//         // 모든 티켓이 삭제되면 클래스를 추가하여 메시지를 표시합니다.
//         if ($(".headcount-input-box").length === 0) {
//             $(".headcount-input-box-none").addClass("On");
//         }
//     });
// });