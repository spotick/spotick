const UserStatus = {
    ACTIVATE: {value: 'ACTIVATE', displayName: "활성"},
    SUSPENDED_7_DAYS: {value: 'SUSPENDED_7_DAYS', displayName: "7일 정지"},
    SUSPENDED_30_DAYS: {value: 'SUSPENDED_30_DAYS', displayName: "30일 정지"},
    PERMANENTLY_SUSPENDED: {value: 'PERMANENTLY_SUSPENDED', displayName: "영구 정지"},
    DEACTIVATE: {value: 'DEACTIVATE', displayName: "탈퇴"}
}

function valueOfUserStatus(key){
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
    $(".tbody-user").on('change','input.user-check',function () {
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

loadUserList();

function loadUserList() {
    fetch(`/admins/user/list`)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            console.table(data.content);
            displayUserLit(data.content);
        });
}

function displayUserLit(data) {
    let text = '';

    data.forEach(user => {
        text += `
            <tr class="user-table-category">
            <td class="user-table-checkbox" align="center">
              <input type="checkbox" name="" class="user-check" id="user-checking" value="${user.id}"/>
            </td>
            <td class="user-email" >${user.email}</td>
            <td class="user-nickname" >${user.nickName}</td>
            <td class="user-palce-tcket">${user.authorityType == 'ROLE_USER'?'일반회원':'관리자'}</td>
            <td class="user-phonenumber">${user.tel==null?'미입력':user.tel}</td>
            <td class="user-register-date">${user.createdDateStr}</td>
            <td class="user-status-select">
              <select class="user-status-choice" name="status-choice">
                <option disabled selected value="">선택</option>
                <option value="ACTIVATE">활성</option>
                <option value="SUSPENDED_7_DAYS">7일 정지</option>
                <option value="SUSPENDED_30_DAYS">30일 정지</option> 
                <option value="PERMANENTLY_SUSPENDED">영구 정지</option> 
                <option value="DEACTIVATE">탈퇴</option> 
              </select>
            </td>
            <td class="user-status-manegement" align="center">
              <div class="user-status-box Y">${valueOfUserStatus(user.userStatus).displayName}</div>
              <!-- 회원 상태 보여주기 -->
              <!-- <div class="user-status-box">정지 회원</div>
              <div class="user-status-box">영구 정지</div> -->
              <!-- 회원 상태 보여주기 -->
            </td>
          </tr>       
       `;
    });

    $('.tbody-user').html(text);
}


