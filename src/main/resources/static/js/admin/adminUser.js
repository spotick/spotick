const UserStatus = {
    ACTIVATE: {value: 'ACTIVATE', displayName: "활성"},
    SUSPENDED_7_DAYS: {value: 'SUSPENDED_7_DAYS', displayName: "7일 정지"},
    SUSPENDED_30_DAYS: {value: 'SUSPENDED_30_DAYS', displayName: "30일 정지"},
    PERMANENTLY_SUSPENDED: {value: 'PERMANENTLY_SUSPENDED', displayName: "영구 정지"},
    DEACTIVATE: {value: 'DEACTIVATE', displayName: "탈퇴"}
}

function valueOfUserStatus(key) {
    return UserStatus[key];
}

// 체크박스 전체 선택
$("#user-table-category-check").on("click", function () {
    if ($(this).is(":checked")) {
        $(".user-check").prop("checked", true);
    } else {
        $(".user-check").prop("checked", false);
    }
});

$(".user-check").on("click", function () {
    if ($(".user-check:checked").length === $(".user-check").length) {
        $("#user-table-category-check").prop("checked", true);
    } else {
        $("#user-table-category-check").prop("checked", false);
    }
});

// 체크박스 선택 후 일반회원 정지회원 설정하기
$(".tbody-user").on('change', 'input.user-check', function () {
    let checkboxes = $("input.user-check:checked");

    checkboxes.each(function () {
        let checkbox = $(this);
        let checkboxIndex = checkbox.closest("tr").index();
        let isChecked = checkbox.is(":checked");

        if (isChecked) {
            $(".user-search-table tbody tr:eq(" + checkboxIndex + ") .user-status-choice").change(function () {
                let selectElement = $(this);
                let selectValue = selectElement.val();
                handleStatusChoice(checkboxIndex, selectValue);
            });
        }
    });
});

function handleStatusChoice(checkboxIndex, selectValue) {
    let statusChoiceBtn = $(".user-search-table tbody tr:eq(" + checkboxIndex + ") .user-status-box");

    if (selectValue === "1") {
        statusChoiceBtn.text("일반 회원").css({
            "background-color": "#eaf4ff",
            color: "#007aff",
        });
    } else if (selectValue === "2") {
        statusChoiceBtn.text("정지 회원").css({
            "background-color": "rgb(255, 237, 237)",
            color: "red",
        });
        // } else if (selectValue === "3") {
        //   statusChoiceBtn.text("정지 회원").css({
        //     "background-color": "rgb(255, 237, 237)",
        //     color: "red",
        //   });
        // } else if (selectValue === "4") {
        //   statusChoiceBtn.text("정지 회원").css({
        //     "background-color": "rgb(255, 237, 237)",
        //     color: "red",
        //   });
    }
}

let page = 0;
let pagingTargetIdx = 3;
let hasNext = true;
let email = '';
let nickName = '';
let status = '';

loadUserList();

function loadUserList() {
    fetch(`/admins/user/list?page=${page++}${createSearchParamQuery()}`)
        .then(response => response.json())
        .then(data => {
            displayUserList(data);
        });
}

function displayUserList(data) {
    hasNext = !data.slice.last;
    let text = '';

    data.slice.content.forEach(user => {
        let isCommonUser = user.authorityType == 'ROLE_USER';
        text += `
            <tr class="user-table-category">
            <td align="center" class="user-table-checkbox" align="center">
              <input type="checkbox" name="" class="user-check" id="user-checking" value="${user.id}"/>
            </td>
            <td align="center" class="user-email" >${user.email}</td>
            <td align="center" class="user-nickname" >${user.nickName}</td>
            <td align="center" class="user-palce-tcket">${isCommonUser ? '일반회원' : '관리자'}</td>
            <td align="center" class="user-phonenumber">${user.tel == null ? '미입력' : user.tel}</td>
            <td align="center" class="user-register-date">${user.createdDate.split('T')[0]}</td>
            <td align="center" class="user-status-select">
              <select class="user-status-choice" name="status-choice">`;
        data.enumValues.forEach(status => {
            text += `<option value="${status.name}" ${user.userStatus === status.name ? 'selected' : ''}>
                        ${status.displayName}
                     </option>`;
        });

        text += `</select>
            </td>
            <td class="user-status-manegement" align="center">`;

        data.enumValues.forEach(status => {
            if (status.name === user.userStatus) {
                text += `<div class="user-status-box ${user.userStatus === "ACTIVATE" ? 'Y' : 'B'}">
                    ${status.displayName}
                  </div>`;
            }
        });
        text += `</td>
            <td class="user-status-manegement" align="center">
                <button class="user-authority-btn ${isCommonUser?'Y':'B'}" data-id="${user.id}" data-isgranted="${isCommonUser}">
                    ${isCommonUser?'권한부여':'권한해제'}
                </button>
            </td>
          </tr>
       `;
    });

    $('.tbody-user').append(text);
}

$('.table-box').on('scroll', function () {
    if (!hasNext) return;

    let itemContainers = document.querySelectorAll('.user-table-category');
    let {bottom} = itemContainers[pagingTargetIdx - 1].getBoundingClientRect();
    let {top} = document.querySelector('.table-box').getBoundingClientRect();
    if (bottom < top) {
        pagingTargetIdx += 12;
        loadPlaceList();
    }
});

// 회원 상태 변경
$('.user-management-btn').on('click', function () {
    let checkboxes = $("input.user-check:checked");
    let statusObjArr = [];

    checkboxes.each((i, checkbox) => {
        let $checkbox = $(checkbox);
        let userId = $checkbox.val();
        let selectedStatus = $checkbox.closest('.user-table-category')
            .find('.user-status-choice').val();
        let statusObj = {userId: userId, status: selectedStatus}
        statusObjArr.push(statusObj);
    });
    changeUserStatus(statusObjArr);
});

function changeUserStatus(statusObjArr) {
    fetch(`/admins/user/status/change`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(statusObjArr),
    }).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        } else {
            clearList();
            loadUserList();
        }
    });
}

function clearList() {
    page = 0;
    pagingTargetIdx = 3;
    $('.tbody-user').html('');
}

function createSearchParamQuery() {
    let paramStr = '';

    paramStr += email === '' ? '' : `&email=${email}`;
    paramStr += nickName === '' ? '' : `&nickName=${nickName}`;
    paramStr += status === '' ? '' : `&status=${status}`;

    return paramStr;
}

$('.search-btn').on('click', function () {
    email = $('#email').val();
    nickName = $('#nickName').val();
    status = $('#status').val();
    clearList();
    loadUserList();
});

// 관리자권한 부여, 해제
$('.tbody-user').on('click','.user-authority-btn',function (){
    let userId = $(this).data('id');
    let isGranted = $(this).data('isgranted');
    if(!confirm(`${userId}번 회원의 관리자 권한을 ${isGranted?'부여':'해제'}하시겠습니까?`)){
        return ;
    }
    fetch(`/admins/user/authority/config`, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            userId:userId,
            isGranted:isGranted
        }),
    }).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        } else {
            clearList();
            loadUserList();
        }
    });
});
















