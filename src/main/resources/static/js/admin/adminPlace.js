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

let page = 0;
let pagingTargetIdx = 2;
let hasNext = true;



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
             <td class="board-public-secret" align="center">
                 <select name="board-status" class="board-status-choice" align="center">
                     <option disabled selected >선택</option>`;
            data.enumValues.forEach(status=>{
            text +=  `<option value="${status.name}">${status.displayName}</option>`;
            });
    text +=  `</select>
             </td>
             <td class="board-status" align="center">`;
        data.enumValues.forEach(status=>{
            if(status.name === place.status){
                text += `<div class="user-status-box ${getStatusStyle(place.status)}">
                    ${status.displayName}
                  </div>`;
            }
        });
                 // <div class="board-status-box Y">활성화</div>
    text +=`</td>
         </tr>
    `;

    });
    $('.tbody-place').append(text);
}

$('.table-box').on('scroll',function (){
    if(!hasNext) return;

    let itemContainers = document.querySelectorAll('.board-table-category');
    let {bottom} = itemContainers[pagingTargetIdx - 1].getBoundingClientRect();
    if (bottom < 0) {
        pagingTargetIdx += 12;
        loadPlaceList();
    }
});


function getStatusStyle(status){
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