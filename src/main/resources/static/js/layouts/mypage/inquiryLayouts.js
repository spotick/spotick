export const inquiryLayouts = (() => {

    const placeInquiryListLayout = (contents) => {
        let html = '';
        contents.forEach((inquiry, i) => {
            const {
                id,
                inquiryId,
                placeTitle,
                price,
                placeAddress,
                placeFileDto,
                evalAvg,
                evalCount,
                bookmarkCount,
                inquiryTitle,
                content,
                isResponded
            } = inquiry;

            const {address} = placeAddress
            const {uploadPath, uuid, fileName} = placeFileDto;

            html += `
                <article id="${inquiryId}">
                    <div class="mpcr-item-container">
                        <div class="mpcr-action-btn">
                            <button onclick="toggleDropdown(this)" type="button">
                                <i aria-hidden="true" class="fa-solid fa-ellipsis"></i>
                                <div class="mpc-dropdown">
                                    <div class="action-item">
                                        <a href="/place/${id}">해당
                                                    게시글로 이동</a>
                                    </div>
                                    <div class="action-item reservationDelete" id="${inquiryId}">
                                        <span>문의 내역 삭제</span>
                                    </div>
                                </div>
                            </button>
                        </div>
                        <div class="mpcr-img-con">
                            <img height="100px" src="/file/display?fileName=${uploadPath}/t_${uuid}_${fileName}" style="width: 180px;" alt="${placeTitle}">
                        </div>
                        <a class="ItemTextContainer" href="/place/${id}">
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
                                <p class="ItemSpaceName">${placeTitle}</p>
                            </div>
                            <div class="ItemPriceContainer">
                                <span class="ItemPrice">${price.toLocaleString()}원</span>
                            </div>
                        </a>
                        <div class="mpcr-divider"></div>
                        <div class="mpcr-info inquiryDetail" idx="${i}" title="상세 정보">
                            <div class="mpcri-title">문의 내용</div>
                            <div class="mpcriq-title">${inquiryTitle}</div>
                            <div class="mpcriq-content">${content}</div>
                        ${isResponded
                ? `<div class="mpcriq-check" title="답변 받음">
                                <img src="/imgs/green_check.svg">
                            </div>`
                : ''
            }
                        </div>
                    </div>
                </article>
            `;
        });

        return html;
    }

    const ticketInquiryListLayout = (contents) => {
        let html = '';
        contents.forEach((inquiry, i) => {
            const {
                ticketId,
                inquiryId,
                title,
                ticketAddress,
                lowestPrice,
                startDate,
                endDate,
                fileName,
                uuid,
                uploadPath,
                inquiryTitle,
                content,
                isResponded
            } = inquiry;

            const {address, addressDetail} = ticketAddress;

            html += `
                <article id="${inquiryId}">
                    <div class="mpcr-item-container">
                        <div class="mpcr-action-btn">
                            <button onclick="toggleDropdown(this)" type="button">
                                <i aria-hidden="true" class="fa-solid fa-ellipsis"></i>
                                <div class="mpc-dropdown">
                                    <div class="action-item">
                                        <a href="/ticket/${ticketId}">해당
                                            게시글로 이동</a>
                                    </div>
                                    <div class="action-item reservationDelete" id="${inquiryId}">
                                        <span>문의 내역 삭제</span>
                                    </div>
                                </div>
                            </button>
                        </div>
                        <a class="mpc-ticket-img-con" href="/ticket/${ticketId}">
                            <img src="/file/display?fileName=${uploadPath}/t_${uuid}_${fileName}">
                        </a>
                        <div class="mpct-card-content" style="min-width: 40%">
                            <div class="mpccc-row">
                                <p class="mpccc-ticket-title general-ellipsis-1">
                                    <a href="/ticket/${ticketId}">${title}</a>
                                </p>
                            </div>
                            <div class="mpccc-row">
                                <a class="mpccc-title-font general-ellipsis-1" href="/ticket/${ticketId}"> 
                                    ${address} ${addressDetail}
                                </a>
                            </div>
                            <div class="mpccc-row" style="margin-top: 10px;">
                                <p class="mpccc-price">${lowestPrice.toLocaleString() + '원'}</p>
                            </div>
                            <div class="mpccc-row" style="margin-top: 10px;">
                                <p class="mpccc-general-font">${formatKoreanDate(startDate)} ~ ${formatKoreanDate(endDate)}</p>
                            </div>
                        </div>
                        <div class="mpcr-divider"></div>
                        <div class="mpcr-info inquiryDetail" idx="${i}" title="상세 정보">
                            <div class="space-between" style="margin-bottom: 8px">
                                <div class="mpcri-title">문의 내용</div>
                            ${isResponded ?
                `<div class="response-check-icon" title="답변 받음">
                                    <img src="/imgs/green_check.svg">
                                </div>`
                : ''
            }
                            </div>
                            <div class="mpcriq-title">${inquiryTitle}</div>
                            <div class="mpcriq-content">${content}</div>
                        </div>
                    </div>
                </article>
            `;
        });

        return html;
    }

    return {
        placeInquiryListLayout: placeInquiryListLayout,
        ticketInquiryListLayout: ticketInquiryListLayout,
    }
})();