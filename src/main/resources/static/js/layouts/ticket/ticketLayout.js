export const ticketLayout = (() => {

    const showTicketList = (tickets) => {
        let html = ``;

        tickets.forEach(ticket => {
            const formattedPrice = ticket.lowestPrice.toLocaleString('ko-KR');

            const startDate = new Date(ticket.startDate);
            const formattedStartDate = startDate.toLocaleDateString('ko-KR');

            const endDate = new Date(ticket.endDate);
            const formattedEndDate = endDate.toLocaleDateString('ko-KR');

            html += `
                <div class="OneItemContainer hover">
                    <div class="OneItemImgContainer">
                        <div class="OneItemImgContainer">
                            <a class="swiper" href="/ticket/detail/${ticket.ticketId}">
                                <div class="swiper-wrapper ImageLength">
                                    <div class="swiper-slide swiper-slide-active" style="width: 287px;">
                                        <img class="ItemImg"
                                             alt=${ticket.ticketTitle}
                                             src="/file/display?fileName=${ticket.uploadPath}/t_${ticket.uuid}_${ticket.fileName}">
                                    </div>
                                </div>
                            </a>
                            <button class="ItemLikeBtn" data-id="${ticket.ticketId}" data-status="${ticket.liked}" type="button">
                                    ${ticket.liked !== true
                                ? '<span>'
                                : '<span class="none">'
                                    }
                                    <img alt="좋아요" src="/imgs/heart_line_white_shadow.d5d214d0.png">
                                </span>
                                    ${ticket.liked === true
                                ? '<span>'
                                : '<span class="none">'
                                    }
                                   <img alt="좋아요" src="/imgs/heart_filled_white_shadow.708fbebd.png">
                                </span>
                            </button>
                        </div>
                        <div class="ItemTextContainer">
                            <div class="ItemHostNameContainer">
                                <span class="ItemHostName">${ticket.postAddress}</span>
                                <div class="ItemCountsContainer">
                                    <div class="ItemsLikeCountContainer">
                                        <img alt="좋아요갯수" class="ItemCountImg"
                                             src="/imgs/heart_line_thin_gray054.1ef56fe5.svg">
                                        <span class="ItemCountText">${ticket.likeCount}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="ItemSpaceNameContainer">
                                <p class="ItemSpaceName">${ticket.ticketTitle}</p>
                            </div>
                            <div class="ItemPriceContainer">
                                <span class="ItemPrice">${formattedPrice}</span>
                                <span class="ItemDate">${formattedStartDate} ~ ${formattedEndDate}</span>
                            </div>
                        </div>
                    </div>
                </div>
           `;
        });

        return html;
    }

    return {
        showTicketList: showTicketList,
    }
})();