let page = 0;
let pagingTargetIdx = 2;
let hasNext = true;


// 체크박스 전체 선택
$("#board-table-category-check").on("click", function () {
    if ($(this).is(":checked")) {
        $(".board-check").prop("checked", true);
    } else {
        $(".board-check").prop("checked", false);
    }
});

$(".board-check").on("click", function () {
    if ($(".board-check:checked").length === $(".board-check").length) {
        $("#board-table-category-check").prop("checked", true);
    } else {
        $("#board-table-category-check").prop("checked", false);
    }
});

// 체크박스 선택 후 글 공개/비공개 설정하기
$(document).ready(function () {
    $("input.board-check").change(function () {
        var checkboxes = $("input.board-check:checked");

        checkboxes.each(function () {
            var checkbox = $(this);
            var checkboxIndex = checkbox.closest("tr").index();
            var isChecked = checkbox.is(":checked");

            if (isChecked) {
                $(".board-search-table tbody tr:eq(" + checkboxIndex + ") .board-status-choice").change(function () {
                    var selectElement = $(this);
                    var selectValue = selectElement.val();
                    handleStatusChoice(checkboxIndex, selectValue);
                });
            }
        });
    });
});

function handleStatusChoice(checkboxIndex, selectValue) {
    var statusChoiceBtn = $(".board-search-table tbody tr:eq(" + checkboxIndex + ") .board-status-box");

    if (selectValue === "1") {
        statusChoiceBtn.text("공개").css({
            "background-color": "#eaf4ff",
            color: "#007aff",
        });
    } else if (selectValue === "2") {
        statusChoiceBtn.text("비공개").css({
            "background-color": "rgb(255, 237, 237)",
            color: "red",
        });
    }
}


loadPlaceList();

function loadPlaceList() {
    fetch(`/admins/place/list?page=${page++}`)
        .then(response => response.json())
        .then(data => {
            displayPlaceList(data);
        });
}

function displayPlaceList(data) {
    hasNext = !data.slice.last;
    let text = '';

    data.slice.content.forEach(place => {
        text += `
         <tr class="board-table-category">
             <td class="board-checkbox" align="center">
                 <input type="checkbox" name="" class="board-check" id="board-checking"/>
             </td>
             <td class="board-id" align="center">${place.placeId}</td>
             <td class="board-writer" align="center">${place.hostEmail}</td>
             <td class="board-title" align="center"><p>${place.title}</p></td>
             <td class="board-write-date" align="center">${place.createdDate.split('T')[0]}</td>
             <td class="board-status" align="center">`;
        data.enumValues.forEach(status => {
            if (status.name === place.status) {
                text += `<div class="user-status-box ${getStatusStyle(place.status)}" data-status="${place.status}">
                    ${status.displayName}
                  </div>
                </td>`;
            }
        });
        text += `<td class="board-public-secret" align="center">
                    ${displayByStatus(place.status)}
                 </td>
            </tr>`;
    });
    $('.tbody-place').append(text);
}

$('.table-box').on('scroll', function () {
    if (!hasNext) return;

    let itemContainers = document.querySelectorAll('.board-table-category');
    let {bottom} = itemContainers[pagingTargetIdx - 1].getBoundingClientRect();
    if (bottom < 0) {
        pagingTargetIdx += 12;
        loadPlaceList();
    }
});

function getStatusStyle(status) {
    switch (status) {
        case 'APPROVED':
            return 'Y';
        case 'REJECTED':
        case 'REPLACED':
        case 'DELETED':
            return 'B';
        default:
            return 'P';
    }
}

function displayByStatus(status) {
    switch (status) {
        // case 'APPROVED':
        //     return `<button type="button" class="change-btn B" data-change="DISABLED">비활성화</button>`;
        // case 'DISABLED':
        //     return `<button type="button" class="change-btn Y" data-change="APPROVED">활성화</button>`;
        case 'REGISTRATION_PENDING':
        case 'MODIFICATION_REQUESTED':
            return `<button type="button" class="approve-btn Y" data-approve="true">승인</button>
            <button type="button" class="approve-btn B" data-approve="false">거절</button>`;
        default:
            return '';
    }
}

// 장소 등록, 수정신청 승인 및 거절
$('.tbody-place').on('click', '.approve-btn', function () {

    if(!confirm(`신청을 ${$(this).text()}하시겠습니까?`)){
        return ;
    }

    let isApprove = $(this).data('approve');
    let $root = $(this).closest('.board-table-category')
    let placeId = $root.find('.board-id').text();
    let $statusBox = $root.find('.user-status-box');
    let status = $statusBox.data('status');

    fetch(`/admins/place/approve`,{
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            isApprove: isApprove,
            placeId: placeId,
            status: status
        }),
    }).then(response=>{
        if(!response.ok){
            throw response;
        }else{
            clearList();
            loadPlaceList();
        }
    });

});

function clearList() {
    page = 0;
    $('.tbody-place').html('');
}


// 장소 상태 변경(활성화/비활성화)
// $('.tbody-place').on('click', '.change-btn', function () {
//
//     let stateToChange = $(this).data('change');
//     let placeId = $(this).closest('.board-table-category').find('.board-id').text();
//
//     if (!confirm(`${placeId}번 장소를 ${$(this).text()}하시겠습니까`)) {
//         return;
//     }
//
//     let isDisabled = stateToChange ==='DISABLED';
//
//     fetch(`/place/api/${stateToChange.toLowerCase()}/${placeId}`, {
//         method: 'PATCH'
//     }).then(response => {
//         if (!response.ok) {
//             throw response;
//         }
//         return response.text();
//     }).then(message=>{
//         $(this).data('change',isDisabled?'APPROVED':'DISABLED');
//         $(this).text(isDisabled?'활성화':'비활성화');
//         $(this).removeClass(isDisabled?'B':'Y').addClass(isDisabled?'Y':'B');
//         let $statusDiv = $(this).closest('.board-table-category').find('.user-status-box');
//         $statusDiv.removeClass(isDisabled?'Y':'B').addClass(isDisabled?'B':'Y');
//         $statusDiv.text(isDisabled?'비활성화':'활성화');
//     });
// });







