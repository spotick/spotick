


$('.visitor-ctr-box button').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= Number($visitors.val());
    let number = visitNum+Number($(this).val());
    $visitors.val(number>99?99:number);
    updateSelectedPeople()
});

$('.plus').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= $visitors.val();
    if(visitNum>=99){
        $visitors.val(99);
        return;
    }
    $visitors.val(++visitNum);
    updateSelectedPeople()
});

$('.minus').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= $visitors.val();
    if(visitNum<=1){
        $visitors.val(1);
        return;
    }
    $visitors.val(--visitNum);
    updateSelectedPeople();
});
updateSelectedPeople();
function updateSelectedPeople(){
    let selectedCount = $('.visitors').val();
    let defaultPeople = $('#defaultPeople').val()
    let additionMsg = selectedCount>defaultPeople? ` (${selectedCount-defaultPeople}명 추가)`:``;

    $('.selected-count').text(`${selectedCount}명${additionMsg}`);
}

// 초기 선택날짜 세팅
setUpCheckInOut(setCheckOutTimes);

// 전달받은 체크인, 체크아웃 시간으로 해당페이지 체크인,체크아웃 시간 초기화
function setUpCheckInOut(setCheckOutTimes){
    let checkInOptionArr = $('#checkIn option');
    let checkInTargetValue = $('#reservationCheckIn').val().slice(11,13);
    let checkOutTargetValue = $('#reservationCheckOut').val().slice(11,13);

    setUpSelectedOption(checkInOptionArr,checkInTargetValue);
    setCheckOutTimes();

    let checkOutOptionArr = $('#checkOut option');

    setUpSelectedOption(checkOutOptionArr,checkOutTargetValue);
}

// 전달받은 체크인, 아웃 시간과 옵션의 값이 같으면 selected
function setUpSelectedOption(options,target){
    options.each((i, option)=>{
        let $tmp = $(option);
        if(Number($tmp.val())===Number(target)){
            $tmp.attr('selected',true);
        }
    });
}

// 체크인 시간에 따라 체크아웃 시간 설정
function setCheckOutTimes() {
    let checkIn = Number($('#checkIn').val());
    let $checkOut = $('#checkOut');
    $checkOut.children().remove();
    let text = '';
    for (let i = 1; i < 24; i++) {
        let time = i + checkIn;
        time = time >= 24 ? time - 24 : time
        if (time === 0) {
            text += `
               <optgroup label="${getNextDay($('#reservationDate').val()).getDate()}일">
                </optgroup>
            `;
        }
        text += `
            <option value="${time}">${convertToAmPmFormat(time)}시</option>
        `;
    }
    $checkOut.append(text);
}

// 날짜 문자열에서 다음 일 반환 yyyy-23-dd -> 24
function getNextDay(dateString) {
    let date = new Date(dateString);
    date.setDate(date.getDate() + 1);
    return date;
}

// 시간 값에 따라 오전 오후 시간으로 변환
function convertToAmPmFormat(hour) {
    let amPm = hour >= 12 ? '오후' : '오전';
    hour = hour % 12;
    hour = hour ? hour : 12; // 시간이 0이면 12로 변환
    return `${amPm} ${hour.toString()}`;
}














