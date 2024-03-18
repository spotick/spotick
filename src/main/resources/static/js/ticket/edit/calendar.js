// Datepicker의 기본 설정을 한국어로 변경
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

const startDate = $('#startDate').val();
const endDate = $('#endDate').val();

// 페이지가 로드될 때 실행할 함수
$(function () {
    // datepicker 설정
    $("#datepicker1").datepicker({
        minDate: startDate,
        maxDate: startDate,
    });

    // 두 번째 데이트피커 설정
    $("#datepicker2").datepicker({
        minDate: endDate,
        maxDate: endDate,
    });
});