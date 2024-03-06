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
$(document).ready(function () {
    $("input.user-check").change(function () {
        var checkboxes = $("input.user-check:checked");

        checkboxes.each(function () {
            var checkbox = $(this);
            var checkboxIndex = checkbox.closest("tr").index();
            var isChecked = checkbox.is(":checked");

            if (isChecked) {
                $(".user-search-table tbody tr:eq(" + checkboxIndex + ") .user-status-choice").change(function () {
                    var selectElement = $(this);
                    var selectValue = selectElement.val();
                    handleStatusChoice(checkboxIndex, selectValue);
                });
            }
        });
    });
});

function handleStatusChoice(checkboxIndex, selectValue) {
    var statusChoiceBtn = $(".user-search-table tbody tr:eq(" + checkboxIndex + ") .user-status-box");

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

getUserList();
function getUserList(){
    fetch(`/admins/user/list`)
        .then(console.log);
}



