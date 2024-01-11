// 체크박스 전체 선택
$("#reply-table-category-check").on("click", function () {
    if ($(this).is(":checked")) {
        $(".reply-check").prop("checked", true);
    } else {
        $(".reply-check").prop("checked", false);
    }
});

$(".reply-check").on("click", function () {
    if ($(".reply-check:checked").length === $(".reply-check").length) {
        $("#reply-table-category-check").prop("checked", true);
    } else {
        $("#reply-table-category-check").prop("checked", false);
    }
});

// 체크박스 선택 후 공개/비공개 설정하기
$(document).ready(function () {
    $("input.reply-check").change(function () {
        var checkboxes = $("input.reply-check:checked");

        checkboxes.each(function () {
            var checkbox = $(this);
            var checkboxIndex = checkbox.closest("tr").index();
            var isChecked = checkbox.is(":checked");

            if (isChecked) {
                $(".reply-search-table tbody tr:eq(" + checkboxIndex + ") .select-public-secret").change(function () {
                    var selectElement = $(this);
                    var selectValue = selectElement.val();
                    handleStatusChoice(checkboxIndex, selectValue);
                });
            }
        });
    });
});

function handleStatusChoice(checkboxIndex, selectValue) {
    var statusChoiceBtn = $(".reply-search-table tbody tr:eq(" + checkboxIndex + ") .reply-status-box");

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
