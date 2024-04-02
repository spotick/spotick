import {getGrade} from "./detail/ticket-detail.js"

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
const $selectedDate = $('#selectedDate');

// 페이지가 로드될 때 실행할 함수
$(function () {
    // datepicker 설정
    $("#datepicker").datepicker({
        minDate: startDate,
        maxDate: endDate,
        onSelect: function (date) {
            $selectedDate.val(date);
            getGrade(date);
        },
    });
});


// 현재 날짜를 가져와서 '년-월-일' 형태로 표시
// let today = new Date();  // 현재 날짜를 가져옴
//
// let year = today.getFullYear();  // 년도를 가져옴
// let month = ('0' + (today.getMonth() + 1)).slice(-2);  // 월을 가져옴 (월은 0부터 시작하므로 1을 더하고, 두 자리 수를 유지하기 위해 앞에 0을 추가)
// let day = ('0' + today.getDate()).slice(-2);  // 일을 가져옴 (두 자리 수를 유지하기 위해 앞에 0을 추가)
//
// let dateString = year + '-' + month + '-' + day;  // 날짜를 '년-월-일' 형태로 만듦
//
// $("#reservationDate").val(dateString);  // 만든 날짜를 input 요소에 표시