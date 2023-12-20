/*
캘린더 사용법

1. buildCalendar() / 캘린더를 생성함(오늘 날짜 기준으로 캘린더가 생성됨)

2. setEventDates() 이후 buildCalendar() / 이벤트날짜(시작일과 종료일)를 설정하고 캘린더 생성시 이벤트 시작일을 기준으로 캘린더가 생성됨,
    시작일과 종료일, 그리고 그 사이의 날짜는 캘린더에 표기됨.

    


모든 날짜 삽입은 Date(timestamp) 기준임
*/




// 장소 빌린 날짜 기준의 캘린더
const calendarService = (function () {
    // 당일 날짜 정보 저장
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    let currentCalenderPage = new Date;
    let eventStartDate = null;
    let eventEndDate = null;

    // 달력 생성 : 해당 달에 맞춰 테이블을 만들고, 날짜를 채워 넣는다.
    function buildCalendar() {
        let firstDate = new Date(currentCalenderPage.getFullYear(), currentCalenderPage.getMonth(), 1);
        let lastDate = new Date(currentCalenderPage.getFullYear(), currentCalenderPage.getMonth() + 1, 0);

        let calendarBody = document.querySelector(".calendar-body");
        document.getElementById("calYear").innerText = currentCalenderPage.getFullYear();
        document.getElementById("calMonth").innerText = putZero(currentCalenderPage.getMonth() + 1);

        while (calendarBody.rows.length > 0) {                        // 이전 출력결과가 남아있는 경우 초기화
            calendarBody.deleteRow(calendarBody.rows.length - 1);
        }

        // 캘린더바디에 첫번째 행을 추가하며 현재 요일만큼 공백을 추가시킨다. (1일이 수요일일시 일,월,화 공백td 추가)
        let nowRow = calendarBody.insertRow();
        for (let j = 0; j < firstDate.getDay(); j++) {
            nowRow.insertCell();
        }
        // 1일부터 해당 달말일까지 td 추가
        for (let nowDay = firstDate; nowDay <= lastDate; nowDay.setDate(nowDay.getDate() + 1)) {

            let nowColumn = nowRow.insertCell();
            nowColumn.innerText = putZero(nowDay.getDate());
            nowColumn.className = 'date';

            // 토요일 출력 이후 새로운 행으로 교체
            if (nowDay.getDay() == 6) {
                nowRow = calendarBody.insertRow();
            }

            // 오늘날짜 표시
            if (nowDay.toDateString() === today.toDateString()) {
                nowColumn.classList.add("today");
            }

            if (eventEndDate !== null) {            
                if (nowDay >= eventStartDate && nowDay <= eventEndDate) {
                    nowColumn.classList.add("event-date");
                }
            }
            
        }
    }

    // 이전달 버튼 클릭
    function prevCalendar() {
        currentCalenderPage = new Date(currentCalenderPage.getFullYear(), currentCalenderPage.getMonth() - 1, currentCalenderPage.getDate());   // 현재 달을 1 감소
        buildCalendar();    // 달력 다시 생성
    }

    // 다음달 버튼 클릭
    function nextCalendar() {
        currentCalenderPage = new Date(currentCalenderPage.getFullYear(), currentCalenderPage.getMonth() + 1, currentCalenderPage.getDate());   // 현재 달을 1 증가
        buildCalendar();    // 달력 다시 생성
    }

    function setEventDates(startDate, endDate) {
        currentCalenderPage = startDate;
        eventStartDate = startDate;
        eventStartDate.setHours(0,0,0,0);
        eventEndDate = endDate;
        eventEndDate.setHours(0,0,0,0);
    }

    // 날짜가 1일 ~ 9일 까지는 '01'과 같은 형식으로 출력되게 해줌. 아닐시 그냥 반환
    function putZero(value) {
        if (value < 10) {
            value = "0" + value;
            return value;
        }
        return value;
    }

    return {
        buildCalendar: buildCalendar,
        prevCalendar: prevCalendar,
        nextCalendar: nextCalendar,
        setEventDates: setEventDates
    }
})();