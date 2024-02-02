let placeId = $('#placeId').val();
let disabledData = null;

$('select').on('change',setReservationFormCheckInAndOut);

$('.visitor-ctr-box button').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= Number($visitors.val());
    let number = visitNum+Number($(this).val());
    $visitors.val(number>99?99:number);
    updateSelectedPeople();
    calculateAmountAndShow();
});

$('.plus').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= $visitors.val();
    if(visitNum>=99){
        $visitors.val(99);
        return;
    }
    $visitors.val(++visitNum);
    updateSelectedPeople();
    calculateAmountAndShow();
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
    calculateAmountAndShow();
});
updateSelectedPeople();
function updateSelectedPeople(){
    let selectedCount =  Number($('#reservationVisitors').val());
    let defaultPeople = Number($('#placeDefaultPeople').val());
    let additionMsg = selectedCount>defaultPeople? ` (${selectedCount-defaultPeople}명 추가)`:``;

    $('.selected-count').text(`${selectedCount}명${additionMsg}`);
}

// 초기 선택날짜 세팅

// 전달받은 체크인, 체크아웃 시간으로 해당페이지 체크인,체크아웃 시간 초기화
function setUpCheckInOut(){

    let checkInOptionArr = $('#checkIn option');

    let checkInTargetValue = $('#reservationCheckIn').val();
    let checkOutTargetValue = $('#reservationCheckOut').val();
    setUpSelectedOption(checkInOptionArr,checkInTargetValue);
    setCheckOutTimes();
    let checkOutOptionArr = $('#checkOut option');
    setUpSelectedOption(checkOutOptionArr,checkOutTargetValue);
}

// 전달받은 체크인, 아웃 시간과 옵션의 값이 같으면 selected
function setUpSelectedOption(options,target){
    options.each((i, option)=>{
        let $tmp = $(option);
        if($tmp.val()===target){
            $tmp.attr('selected',true);
        }
    });
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

// 초기 결제금액 세팅
calculateAmountAndShow();
function calculateAmountAndShow() {
    let checkIn = new Date($('#checkIn').val()).getHours();
    let checkOut = new Date($('#checkOut').val()).getHours();
    let usageTime = getUsageTime(checkIn,checkOut);
    let placeSurcharge = $('#placeSurcharge').val();
    let visitors = Number($('.visitors').val());
    let defaultPeople = Number($('#placeDefaultPeople').val());
    let surcharge = defaultPeople < visitors
        ? placeSurcharge * (visitors - defaultPeople) : 0;
    let placePrice = $('#placePrice').val();
    let totalAmount = placePrice * usageTime;
    let $peopleAmountBox = $('.people-amount-box');

    let reservationAmount = totalAmount+surcharge;

    $('.reservation-amount').text(totalAmount.toLocaleString());
    if (surcharge>0){
        $peopleAmountBox.html(`
             <p class="info-title">인원 추가 금액</p>
             <div class="flex-column">
                 <p class="amount">+ <span>${surcharge.toLocaleString()}</span> 원</p>
                 <p class="payment-guide">${visitors-defaultPeople}명 추가(인당${placeSurcharge}원)</p>
             </div>
        `);
    }else{
        $peopleAmountBox.html('');
    }
    $('.total-amount').text(`${reservationAmount.toLocaleString()}원`);
    $('#reservationAmount').val(reservationAmount);
}

function getUsageTime(checkInTime, checkOutTime) {
    let usageTime = checkOutTime - checkInTime;
    return usageTime <= 0 ? usageTime + 24 : usageTime;
}

function setReservationFormCheckInAndOut() {
    let checkInTime = $('#checkIn').val();
    let checkOutTime = $('#checkOut').val();
    $('#reservationCheckIn').val(checkInTime);
    $('#reservationCheckOut').val(checkOutTime);
    calculateAmountAndShow();
}

getReservedTimes();

$(`#reservationDate`).on('change', function (){
    $('#reservationCheckIn').val('');
    $('#reservationCheckOut').val('');
    getReservedTimes();
});

function getReservedTimes() {
    let reservationDate = $('#reservationDate').val();
    $('#checkIn').html(`<option selected disabled>시작 시간</option>`);
    $('#checkOut').html(`<option selected disabled>종료 시간</option>`);
    setCheckInTimes();
    fetch(`/reservations/v1/places/${placeId}/reserved-times?reservationDate=${reservationDate}`)
        .then(r => r.json())
        .then(data=>disabledData=data)
        .then(data=>setUpCheckInOut())
        .then(disableReservedTime);
}

function setCheckInTimes() {
    let $checkIn = $('#checkIn');
    let reserveDate = $('#reservationDate').val();

    $checkIn.children(':not(:first-child)').remove();
    let text = '';

    for (let i = 0; i < 24; i++) {

        text += `
            <option value="${reserveDate} ${i.toString().padStart(2, '0')}:00:00">${convertToAmPmFormat(i)}시</option>
        `;
    }
    $checkIn.append(text);
}

function setCheckOutTimes() {
    let checkIn = new Date($('#checkIn').val()).getHours();
    let $checkOut = $('#checkOut');
    let reserveDate = $('#reservationDate').val();

    $checkOut.children(':not(:first-child)').remove();
    let text = '';
    for (let i = 1; i < 24; i++) {
        let time = i + checkIn;
        time = time >= 24 ? time - 24 : time
        if (time === 0) {
            text += `
               <optgroup label="${getNextDay(reserveDate).getDate()}일">
                </optgroup>
            `;
            reserveDate = getNextDateFormat(reserveDate);
        }
        text += `
            <option value="${reserveDate} ${time.toString().padStart(2, '0')}:00:00">${convertToAmPmFormat(time)}시</option>
        `;
    }
    $checkOut.append(text);

    $('#checkOut option').each((i, opt) => {
        let $opt = $(opt);
        let optValue = new Date($opt.val());

        disabledData.forEach(d => {
            let checkIn = new Date(d.checkIn);
            let checkOut = new Date(d.checkOut);
            if (checkIn <= optValue && optValue < checkOut) {
                $opt.attr('disabled', true);
            }
        });
    })

}

function getNextDateFormat(dateString) {
    let date = new Date(dateString);
    date.setDate(date.getDate() + 1);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

function disableReservedTime(data) {
    let $checkInOpts = $('#checkIn option');

    $checkInOpts.each((i, opt) => {
        let $opt = $(opt);
        let optValue = new Date($opt.val());

        disabledData.forEach(d => {
            let checkIn = new Date(d.checkIn);
            let checkOut = new Date(d.checkOut);
            if (checkIn <= optValue && optValue < checkOut) {
                $opt.attr('disabled', true);
            }
        });
    });
}

$('.payment-btn').on('click',function (){

    if ($('#reservationCheckIn').val()===''||$('#reservationCheckOut').val()===''){
        alert('예약 시간을 선택해주세요');
        return ;
    }
    $('#reservationForm').submit();
});



















