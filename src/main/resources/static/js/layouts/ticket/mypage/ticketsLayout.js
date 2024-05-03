const mypageTicketLayout = async (dto) => {
    const {
        ticketId,
        title,
        ticketAddress,
        ticketCategory,
        minPrice,
        startDate,
        endDate,
        fileName,
        uuid,
        uploadPath,
        inquiriesCount,
    } = dto;
    const {address, addressDetail} = ticketAddress;

    return `
         <div class="mpctd-title">
             <p>${title}</p>
         </div>

         <p>행사 장소 주소</p>
         <label class="mpctd-input-con" style="margin-bottom: 8px">
             <input value="${address}" readonly type="text">
         </label>
         <label class="mpctd-input-con" style="margin-bottom: 40px">
             <input value="${addressDetail}" readonly type="text">
         </label>

         <p>날짜 선택</p>
         <div class="date-pick-container" id="detailDates"></div>

         <p>티켓 판매 현황</p>
         <div class="mpctd-ticket-container">
             <table class="ticket-table">
                 <thead>
                 <tr>
                     <td>등급</td>
                     <td>가격</td>
                     <td>판매수</td>
                     <td>총 좌석수</td>
                 </tr>
                 </thead>
                 <tbody id="detailGrades">
                 </tbody>
                 <div class="loading-wrap" id="loadingMark">
                    <img alt="" src="/imgs/loading.svg">
                 </div>
             </table>
         </div>
    `;
}

const showGradeInfo = (data) => {
    let html = '';

    data.forEach(grade => {
        const {gradeName, price, sold, maxPeople} = grade;

        html += `
            <tr>
                <td>${gradeName}</td>
                <td>${price.toLocaleString()}</td>
                <td>${sold}</td>
                <td>${maxPeople}</td>
            </tr>
        `;
    });

    return html;
}

export {mypageTicketLayout, showGradeInfo}