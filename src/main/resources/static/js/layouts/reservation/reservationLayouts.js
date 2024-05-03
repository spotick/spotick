import {formatKoreanDatetime} from "../../utils/timeUtils.js";

export const reservationLayouts = (() => {

    const reservationListLayout = (data) => {
        const contents = data.content;
        const isLast = data.last;
        let html = ``;

        contents.forEach((reservation) => {
            const {
                id,
                checkIn,
                checkOut,
                nickname,
                fileName,
                uuid,
                uploadPath,
                defaultImage
            } = reservation;

            html += `
                <div class="mpcp-item reservation" rId="${id}">
                    <div class="mpcpi-top">
                        <div class="mpcpi-request-user-con">
                            <div class="mpcpi-ruser-icon">
                            ${defaultImage ?
                                `<img src="/file/default/display?fileName=${fileName}">` :
                                `<img src="/file/display?fileName=${uploadPath}/t_${uuid}_${fileName}">`
                            }
                            </div>
                            <span class="mpcp-ruser-name">${nickname}</span>
                        </div>
                    </div>
                    <div class="mpcp-body">
                        <div class="mpcp-content">
                            <span>${formatKoreanDatetime(checkIn)}</span>
                            <span style="margin: 0 10px;">~</span>
                            <span>${formatKoreanDatetime(checkOut)}</span>
                        </div>
                        <div class="mpcp-btn detailOpen" rId="${id}">   
                            <span>상세보기</span>
                        </div>
                    </div>
                </div>
            `;
        });

        return {isLast, html, contents};
    }

    return {
        reservationListLayout: reservationListLayout,
    }
})();