// 체크박스 전체 선택
$("#approval-category-check").on("click", function () {
    if ($(this).is(":checked")) {
        $(".approval-check").prop("checked", true);
    } else {
        $(".approval-check").prop("checked", false);
    }
});

$(".approval-check").on("click", function () {
    if ($(".approval-check:checked").length === $(".approval-check").length) {
        $("#approval-category-check").prop("checked", true);
    } else {
        $("#approval-category-check").prop("checked", false);
    }
});

// 체크박스 선택 후 승인상태 설정하기
$(document).ready(function () {
    $("input.approval-check").change(function () {
        var checkboxes = $("input.approval-check:checked");

        checkboxes.each(function () {
            var checkbox = $(this);
            var checkboxIndex = checkbox.closest("tr").index();
            var isChecked = checkbox.is(":checked");

            if (isChecked) {
                $(".approval-search-table tbody tr:eq(" + checkboxIndex + ") .approval-status-choice").change(function () {
                    var selectElement = $(this);
                    var selectValue = selectElement.val();
                    handleStatusChoice(checkboxIndex, selectValue);
                });
            }
        });
    });
});

function handleStatusChoice(checkboxIndex, selectValue) {
    var statusChoiceBtn = $(".approval-search-table tbody tr:eq(" + checkboxIndex + ") .approval-status-box");

    if (selectValue === "1") {
        statusChoiceBtn.text("승인완료").css({
            "background-color": "#eaf4ff",
            color: "#007aff",
        });
    } else if (selectValue === "2") {
        statusChoiceBtn.text("승인거절").css({
            "background-color": "rgb(255, 237, 237)",
            color: "red",
        });
    }
}
