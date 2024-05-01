import {getPlaceStatusName} from "../../utils/place-status.js";

export const modalLayouts = (() => {

    const placeReservationDetailModalLayout = async (dto) => {
        const {
            id,
            title,
            price,
            placeAddress,
            placeFileDto,
            evalAvg,
            evalCount,
            bookmarkCount,
            visitors,
            checkIn,
            checkOut,
            content,
            reservationStatus
        } = dto;

        const {
            uploadPath,
            uuid,
            fileName
        } = placeFileDto;

        const {address} = placeAddress;

        return `
            <div>
                <a href="/place/${id}" style="display: flex; margin-bottom: 20px;">
                    <div class="mpcr-img-con">
                        <img style="width: 180px;" alt="${fileName}" src="/file/display?fileName=${uploadPath}/t_${uuid}_${fileName}">
                    </div>
                    <div class="ItemTextContainer">
                        <div class="ItemHostNameContainer">
                            <span class="ItemHostName">${address}</span>
                            <div class="ItemCountsContainer">
                                <div class="ItemsStarCountContainer">
                                    <img alt="후기갯수" class="ItemCountImg"
                                         src="/imgs/star_filled_paintYellow056.a8eb6e44.svg">
                                    <span class="ItemCountText">${evalAvg.toFixed(1)}(${evalCount})</span>
                                </div>
                                <div class="ItemsLikeCountContainer">
                                    <img alt="북마크갯수" class="ItemCountImg bookmark"
                                         src="/imgs/bookmark_thin.svg">
                                    <span class="ItemCountText">${bookmarkCount}</span>
                                </div>
                            </div>
                        </div>
                        <div class="ItemSpaceNameContainer">
                            <p class="ItemSpaceName">${title}</p>
                        </div>
                        <div class="ItemPriceContainer">
                            <span class="ItemPrice" id="detailPrice">${price}</span>
                        </div>
                        <div class="mpccc-row content-between" style="margin-top: 10px;">
                            <div class="mpccc-status">
                                <span>${getPlaceStatusName(reservationStatus)}</span>
                            </div>
                        </div>
                    </div>
                </a>
                <h2 class="mri-title">예약 정보</h2>
                <div class="mri-content">
                    <div>
                        <div class="mri-input-title">사용 인원</div>
                        <div class="mri-input-container" style="width: 100px; margin-bottom: 20px;">
                            <input value="${visitors}" readonly type="text">
                            <span>명</span>
                        </div>
                        <div class="mri-input-title">내 요청 사항</div>
                        <div class="mri-input-container">
                            <textarea class="mri-textarea" cols="30" readonly 
                                      rows="10">${content}</textarea>
                        </div>
                    </div>
                    <div class="mri-divider"></div>
                    <div>
                        <table class="calendar">
                            <thead class="calendar-head">
                            <tr class="ch-title">
                                <td id="prevCalendar" style="cursor:pointer;">&#60;</td>
                                <td colspan="5">
                                    <span id="calYear"></span>년
                                    <span id="calMonth"></span>월
                                </td>
                                <td id="nextCalendar" style="cursor:pointer;">&#62;</td>
                            </tr>
                            <tr class="ch-day">
                                <td>일</td>
                                <td>월</td>
                                <td>화</td>
                                <td>수</td>
                                <td>목</td>
                                <td>금</td>
                                <td>토</td>
                            </tr>
                            </thead>
                            <tbody class="calendar-body">
                            </tbody>
                        </table>
                        <div class="mri-input-title">대여 시작시간</div>
                        <div class="mri-input-container" style="margin-bottom: 10px;">
                            <input readonly type="text" value="${checkIn}">
                        </div>
                        <div class="mri-input-title">대여 시작시간</div>
                        <div class="mri-input-container">
                            <input id="detailCheckOut" readonly type="text" value="${checkOut}">
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    const inquiryDetailModalLayout = async (dto) => {
        const {content, response, responded} = dto;

        let responseTextarea = '';
        if (responded) {
            responseTextarea = response;
        } else {
            responseTextarea = "아직 답변받지 못했습니다!";
        }

        return `
            <div class="modal-place-content-container">
                <div class="modal-place-con-box">
                    <div class="modal-place-title">문의 내용</div>
                    <div class="modal-place-input-con">
                        <textarea class="modal-place-txarea" cols="30" readonly rows="10">${content}</textarea>
                    </div>
                    <div class="modal-place-title">답변</div>
                    <div class="modal-place-input-con">
                        <textarea class="modal-place-txarea" cols="30" readonly rows="10">${responseTextarea}</textarea>
                    </div>
                </div>
            </div>
        `;
    }


    return {
        placeReservationDetailModalLayout: placeReservationDetailModalLayout,
        inquiryDetailModalLayout: inquiryDetailModalLayout,
    }
})();