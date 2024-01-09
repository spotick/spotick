// 체크박스 전체 선택
$("#pay-management-category-check").on("click", function () {
    if ($(this).is(":checked")) {
        $(".payment-check").prop("checked", true);
    } else {
        $(".payment-check").prop("checked", false);
    }
});

$(".payment-check").on("click", function () {
    if ($(".payment-check:checked").length === $(".payment-check").length) {
        $("#pay-management-category-check").prop("checked", true);
    } else {
        $("#pay-management-category-check").prop("checked", false);
    }
});

// 체크박스 선택 후 구매상태 설정하기
$(document).ready(function () {
    $("input.payment-check").change(function () {
        var checkboxes = $("input.payment-check:checked");

        checkboxes.each(function () {
            var checkbox = $(this);
            var checkboxIndex = checkbox.closest("tr").index();
            var isChecked = checkbox.is(":checked");

            if (isChecked) {
                $(".payment-search-table tbody tr:eq(" + checkboxIndex + ") .product-status-choice").change(function () {
                    var selectElement = $(this);
                    var selectValue = selectElement.val();
                    handleStatusChoice(checkboxIndex, selectValue);
                });
            }
        });
    });
});

function handleStatusChoice(checkboxIndex, selectValue) {
    var statusChoiceBtn = $(".payment-search-table tbody tr:eq(" + checkboxIndex + ") .payment-status-box");

    if (selectValue === "1") {
        statusChoiceBtn.text("구매완료").css({
            "background-color": "#eaf4ff",
            color: "#007aff",
        });
    } else if (selectValue === "2") {
        statusChoiceBtn.text("구매취소").css({
            "background-color": "rgb(255, 237, 237)",
            color: "red",
        });
    }
}
