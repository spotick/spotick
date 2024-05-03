import {getPlaceStatusName, getReservationStatusName} from "../../status/status.js";
import {formatKoreanDatetime} from "../../utils/timeUtils.js";

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

    const reviewWriteFormModalLayout = async (dto) => {
        const {
            id,
            reservationId,
            title,
            price,
            placeAddress,
            placeFiles,
            evalAvg,
            evalCount,
            bookmarkCount,
            visitors,
            checkIn,
            checkOut,
            content,
            reservationStatus
        } = dto;
        const {address} = placeAddress;
        const {uploadPath, uuid, fileName} = placeFiles[0];

        return `
            <div style="display: flex; margin-bottom: 20px;">
                <div class="mpcr-img-con">
                    <img height="100px" style="width: 180px;" alt="${title}" src="/file/display?fileName=${uploadPath}/t_${uuid}_${fileName}">
                </div>
                <div class="ItemTextContainer" style="min-width: 300px; max-width: 300px">
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
                        <span class="ItemPrice">${price}</span>
                    </div>
                    <div class="mpccc-row content-between" style="margin-top: 10px;">
                        <div class="mpccc-status">
                            <span>${getReservationStatusName(reservationStatus)}</span>
                        </div>
                    </div>
                </div>
                <div class="mpcr-divider"></div>
                <div class="mpcr-info">
                    <div class="mpcri-title">예약 정보</div>
                    <div class="mpcri-date">
                        <span>${formatKoreanDatetime(checkIn)}</span>
                        <span style="margin: 0 4px; color: #868686;">~</span>
                        <span>${formatKoreanDatetime(checkOut)}</span>
                    </div>
                    <div class="mpcri-people-count">
                        <div>
                            사용 인원: <span>${visitors}</span>명
                        </div>
                    </div>
                    <div class="mpcri-content">${content}</div>
                </div>
            </div>
            <h2 class="mri-title">후기작성</h2>
            <input type="hidden">
            <div class="mrf-star-rating-con">
                <span class="mrf-star on" value="1"></span>
                <span class="mrf-star" value="2"></span>
                <span class="mrf-star" value="3"></span>
                <span class="mrf-star" value="4"></span>
                <span class="mrf-star" value="5"></span>
                <div class="mrf-rating-count-con">
                    <input id="reviewScoreInput" readonly value="1" type="text">점
                </div>
            </div>
            <div class="mr-form">
            <textarea cols="30" maxlength="200" id="reviewWriteTxArea" 
                      placeholder="최소 10자 이상 입력해주세요"
                      rows="10" spellcheck="false"></textarea>
            </div>
            <div class="space-between">
                <div class="error-content" id="errorContent"></div>
                <div class="mr-type-counter">
                    <span>글자수</span>
                    <span id="typeCounter">0</span>
                    <span>/ 200</span>
                </div>
            </div>
            <div style="display: flex; justify-content: center;">
                <button class="modal-confirm-btn" disabled id="reviewWriteButton" rId="${reservationId}">
                    <p>작성 완료</p>
                </button>
            </div>
        `;
    }

    const reviewEditFormModalLayout = (dto) => {
        const {
            reviewId,
            score,
            content,
        } = dto;

        return `
            <div>
                <h2 class="mri-title">후기 수정</h2>
                <div class="mrf-star-rating-con">
                ${Array.from({length: 5}, (_, index) => `
                    <span class="mrf-star ${index < score ? 'on' : ''}" value="${index + 1}"></span>
                `).join('')}
                    <div class="mrf-rating-count-con">
                        <input id="reviewScoreInput" value="${score}" readonly type="text">점
                    </div>
                </div>
                <div class="mr-form">
                    <textarea cols="30" id="reviewWriteTxArea" maxlength="200" placeholder="최소 10자 이상 입력해주세요"
                              rows="10" spellcheck="false">${content}</textarea>
                </div>
                <div class="space-between">
                    <div class="error-content" id="errorLine"></div>
                    <div class="mr-type-counter">
                        <span>글자수</span>
                        <span id="typeCounter">${content.length}</span>
                        <span>/ 200</span>
                    </div>
                </div>
                <div style="display: flex; justify-content: center;">
                    <button class="modal-confirm-btn" id="reviewWriteButton" rId="${reviewId}">
                        <p>작성 완료</p>
                    </button>
                </div>
            </div>
        `;
    }

    const reservationRequestModalLayout = (dto) => {
        const {
            id,
            visitors,
            checkIn,
            checkOut,
            amount,
            content,
            nickname,
            fileName,
            uuid,
            uploadPath,
            defaultImage
        } = dto;

        return `
             <div class="modal-place-user-con">
                <div class="modal-user-icon">
                    <img alt="${nickname}" ${defaultImage ? `src="/file/default/display?fileName=${fileName}"` : `src="/file/display?fileName=${uploadPath}/t_${uuid}_${fileName}`}>
                </div>
                <span class="modal-user-name">${nickname}</span>
            </div>
            <div class="modal-place-content-container">
                <div class="modal-place-con-box">
                    <div style="display: flex;">
                        <div style="margin-right: 50px">
                            <div class="modal-place-title">사용 인원</div>
                            <div class="modal-place-input-con" style="width: 100px; margin-bottom: 20px;">
                                <input value="${visitors}" readonly type="text">
                                <span>명</span>
                            </div>
                        </div>
                        <div>
                            <div class="modal-place-title">총 지불 비용</div>
                            <div class="modal-place-input-con" style="width: 100px; margin-bottom: 20px;">
                                <input value="${amount}" readonly type="text">
                            </div>
                        </div>
                    </div>
                    <div class="modal-place-title">요청 사항</div>
                    <div class="modal-place-input-con">
                            <textarea class="mri-textarea" cols="30" id="detailContent" readonly
                                      rows="10">${content}</textarea>
                    </div>
                </div>
                <div class="modal-place-con-box">
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
                        <input value="${formatKoreanDatetime(checkIn)}" readonly type="text">
                    </div>
                    <div class="mri-input-title">대여 시작시간</div>
                    <div class="mri-input-container">
                        <input value="${formatKoreanDatetime(checkOut)}" readonly type="text">
                    </div>
                </div>
            </div>
            <div class="modal-place-btn-wrap" rId="${id}">
                <button class="modal-place-btn" id="approveRequestBtn"
                        style="background-color: #007aff; margin-right: 20px;"
                        type="button">
                    <span>승인</span>
                </button>
                <button class="modal-place-btn" id="rejectRequestBtn"
                    style="background-color: #ffa200;" type="button">
                    <span>거절</span>
                </button>
            </div>
        `;
    }

    const inquiryRequestModalLayout = (dto) => {
        const {
            id,
            inquiryTitle,
            content,
            nickname,
            fileName,
            uuid,
            uploadPath,
            defaultImage
        } = dto;

        return `
            <div class="modal-place-user-con">
                <div class="modal-user-icon">
                    <img alt="${nickname}" ${defaultImage ? `src="/file/default/display?fileName=${fileName}"` : `src="/file/display?fileName=${uploadPath}/t_${uuid}_${fileName}`}>
                </div>
                <span class="modal-user-name">${nickname}</span>
            </div>
            <div class="modal-place-content-container">
                <div class="modal-place-con-box">
                    <div class="modal-place-title">요청 사항</div>
                    <div class="modal-place-input-con">
                        <textarea class="modal-place-txarea" cols="30" readonly
                                rows="10">${content}</textarea>
                    </div>
                    <div class="modal-place-title">답변 작성</div>
                    <div class="modal-place-input-con">
                        <textarea class="modal-place-txarea" cols="30" id="response"
                                placeholder="최소 10자 이상 입력해주세요." rows="10"></textarea>
                    </div>
                    <div class="space-between">
                        <div class="error-content" id="errorContent"></div>
                        <div class="mr-type-counter">
                            <span>글자수</span>
                            <span id="typeCounter">0</span>
                            <span>/ 200</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-place-btn-wrap" style="justify-content: center;">
                <button class="modal-place-btn green" id="requestBtn" iId="${id}" disabled type="button">
                    <span>작성 완료</span>
                </button>
            </div>
        `;
    }

    return {
        placeReservationDetailModalLayout: placeReservationDetailModalLayout,
        inquiryDetailModalLayout: inquiryDetailModalLayout,
        reviewWriteFormModalLayout: reviewWriteFormModalLayout,
        reviewEditFormModalLayout: reviewEditFormModalLayout,
        reservationRequestModalLayout: reservationRequestModalLayout,
        inquiryRequestModalLayout: inquiryRequestModalLayout,
    }
})();