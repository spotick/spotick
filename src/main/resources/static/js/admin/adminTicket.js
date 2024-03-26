let page = 0;
let pagingTargetIdx = 3;
let hasNext = true;
let email = '';
let ticketTitle = '';
let searchStatus = '';


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

loadTicketList();

function loadTicketList() {
    fetch(`/admins/ticket/list?page=${page++}${createSearchParamQuery()}`)
        .then(response => response.json())
        .then(data => {
            displayTicketList(data);
        });
}

function createSearchParamQuery() {
    let paramStr = '';

    paramStr += email === '' ? '' : `&email=${email}`;
    paramStr += ticketTitle === '' ? '' : `&postTitle=${ticketTitle}`;
    paramStr += searchStatus === '' ? '' : `&status=${searchStatus}`;

    return paramStr;
}


// 장소 검색
$('.search-btn').on('click', function () {
    email = $('#email').val();
    ticketTitle = $('#ticketTitle').val();
    searchStatus = $('#status').val();
    clearList();
    loadTicketList();
});


function displayTicketList(data) {
    hasNext = !data.slice.last;
    let text = '';

    data.slice.content.forEach(ticket => {
        text += `
         <tr class="board-table-category">
             <td class="board-id" align="center">${ticket.postId}</td>
             <td class="board-writer" align="center">${ticket.hostEmail}</td>
             <td class="board-title" align="center"><a href="/ticket/detail/${ticket.postId}">${ticket.title}</a></td>
             <td class="board-write-date" align="center">${ticket.createdDate.split('T')[0]}</td>
             <td class="board-status" align="center">`;
        data.enumValues.forEach(status => {
            if (status.name === ticket.status) {
                text += `<div class="user-status-box ${getStatusStyle(ticket.status)}" data-status="${ticket.status}">
                    ${status.displayName}
                  </div>
                </td>`;
            }
        });
        text += `<td class="board-public-secret" align="center">
                    ${displayByStatus(ticket.status)}
                 </td>
            </tr>`;
    });
    $('.tbody-ticket').append(text);
}

$('.table-box').on('scroll', function () {
    if (!hasNext) return;

    let itemContainers = document.querySelectorAll('.board-table-category');
    let {bottom} = itemContainers[pagingTargetIdx - 1].getBoundingClientRect();
    let {top} = document.querySelector('.table-box').getBoundingClientRect();
    if (bottom < top) {
        pagingTargetIdx += 12;
        loadTicketList();
    }
});

function getStatusStyle(status) {
    switch (status) {
        case 'APPROVED':
            return 'Y';
        case 'REJECTED':
        case 'RETicketD':
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
$('.tbody-ticket').on('click', '.approve-btn', function () {

    if (!confirm(`신청을 ${$(this).text()}하시겠습니까?`)) {
        return;
    }

    let isApprove = $(this).data('approve');
    let $root = $(this).closest('.board-table-category')
    let postId = $root.find('.board-id').text();
    let $statusBox = $root.find('.user-status-box');
    let status = $statusBox.data('status');

    fetch(`/admins/ticket/approve`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            isApprove: isApprove,
            postId: postId,
            status: status
        }),
    }).then(response => {
        if (!response.ok) {
            throw response;
        } else {
            clearList();
            loadTicketList();
        }
    });
});

function clearList() {
    page = 0;
    pagingTargetIdx = 3;
    $('.tbody-ticket').html('');
}

