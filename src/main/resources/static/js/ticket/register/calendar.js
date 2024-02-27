$.datepicker.setDefaults({
    dateFormat: 'yy-mm-dd',
    prevText: '이전 달',
    nextText: '다음 달',
    monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    dayNames: ['일', '월', '화', '수', '목', '금', '토'],
    dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
    dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
    showMonthAfterYear: true,
    yearSuffix: '년'
});

$(document).ready(function () {
    let currentDate = new Date();

    // 첫 번째 데이트피커 설정
    $("#datepicker1").datepicker({
        minDate: currentDate,  // 선택할 수 있는 최소 날짜를 현재 날짜로 설정
        onSelect: function (dateText) {
            updateDateInfo(1);
            $("#datepicker2").datepicker("option", "minDate", dateText);  // 두 번째 데이트피커의 최소 날짜 설정
        },
        beforeShowDay: function (date) {
            let reservationDate = $("#reservationDate").datepicker('getDate');
            if (reservationDate !== null) {
                if (date.getTime() === reservationDate.getTime()) {
                    return [true, 'reservationDate'];
                }
            }
            return [true, ''];
        },
        onClose: function () {
            addCompleteButton(1);
        }
    });

    // 두 번째 데이트피커 설정
    $("#datepicker2").datepicker({
        minDate: currentDate,
        onSelect: function (dateText) {
            updateDateInfo(2);
            $("#datepicker1").datepicker("option", "maxDate", dateText); // 두 번째 데이트피커 선택 시 첫 번째 데이트피커의 최대 날짜 설정
        },
        beforeShowDay: function (date) {
            let reservationDate = $("#reservationDate").datepicker('getDate');
            if (reservationDate !== null) {
                if (date.getTime() === reservationDate.getTime()) {
                    return [true, 'reservationDate'];
                }
            }
            return [true, ''];
        },
        onClose: function () {
            addCompleteButton(2);
        }
    });
});

// 시작날짜와 마지막날짜를 표시하는 함수
function updateDateInfo(pickerNumber) {
    let startDate = $("#datepicker1").datepicker("getDate");
    let endDate = $("#datepicker2").datepicker("getDate");

    let isoStartDate = $.datepicker.formatDate("yy-mm-dd", startDate);
    let isoEndDate = $.datepicker.formatDate("yy-mm-dd", endDate);

    console.log("endDate ::: ", isoEndDate);

    if (startDate && endDate) {
        let dateInfo = isoStartDate + " ~ " + isoEndDate;
        $("#date-info").text(dateInfo);

        // 각 데이트피커에 선택된 날짜 범위를 표시
        $("#reservationDate" + pickerNumber).val(dateInfo);
    }

    $('input[name="startDate"]').val(isoStartDate);
    $('input[name="endDate"]').val(isoEndDate);
}