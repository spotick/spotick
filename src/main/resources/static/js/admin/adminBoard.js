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
