export const placeLayouts = (() => {

    const placeListLayout = (data) => {
        const contents = data.content;
        const isLast = data.last;
        let html = '';

        contents.forEach(place => {
            html += `
                <div class="OneItemContainer hover">
                    <div class="OneItemImgContainer">
                        <div class="swiper ImageSwiper swiper-initialized swiper-horizontal swiper-pointer-events swiper-backface-hidden">
                            <a href="/place/detail/${place.id}"
                               class="swiper-wrapper ImageLength" style="transform: translate3d(0px, 0px, 0px);" >
            `;
            place.placeFiles.forEach(placeImg => {
                html += `
                                <div class="swiper-slide swiper-slide-active" style="width: 287px;">
                                    <img class="ItemImg"
                                         height="1350.6666666666665px" 
                                         alt="${place.title}" src="/file/display?fileName=${placeImg.uploadPath}/t_${placeImg.uuid}_${placeImg.fileName}">
                                </div>
                `;
            });
            html += `
                            </a>
                            <div class="NavigationBtnContainer">
                                <button class="NavigationBtn RightBtn" type="button">
                                    <img alt="다음" src="/imgs/round_arrow_right_gray024.7f7e18a3.svg">
                                </button>
                                <button class="NavigationBtn LeftBtn" type="button">
                                    <img alt="이전" src="/imgs/round_arrow_left_gray024.707193e8.svg">
                                </button>
                            </div>
                            <div class="ItemImgPagination">
                                <p><span class="snapIndex">1</span>/5</p>
                            </div>
                        </div>
                        <button class="ItemBookMarkBtn" data-placeid="${place.id}" data-status="${place.bookmarkChecked}" type="button">
                            <span class="${!place.bookmarkChecked ? '' : 'none'}">
                                <i class="fa-regular fa-bookmark"></i>
                            </span>
                            <span class="${place.bookmarkChecked ? '' : 'none'}">
                                <i class="fa-solid fa-bookmark" style="color: white"></i>
                            </span>
                        </button>
                    </div>
                    <div class="ItemTextContainer">
                        <div class="ItemHostNameContainer">
                            <span class="ItemHostName">${place.placeAddress.address}</span>
                            <div class="ItemCountsContainer">
                                <div class="ItemsStarCountContainer">
                                    <img alt="후기갯수" class="ItemCountImg"
                                         src="/imgs/star_filled_paintYellow056.a8eb6e44.svg">
                                    <span class="ItemCountText">${place.evalAvg}(${place.evalCount})</span>
                                </div>
                                <div class="ItemsLikeCountContainer">
                                    <img alt="북마크갯수" class="bookmark-img" src="/imgs/bookmark_thin.svg">
                                    <span class="ItemCountText bookmark-count">${place.bookmarkCount}</span>
                                </div>
                            </div>
                        </div>
                        <div class="ItemSpaceNameContainer">
                            <p class="ItemSpaceName" >
                                <a href="/place/detail/${place.id}">${place.title}</a>
                            </p>
                        </div>
                        <div class="ItemPriceContainer">
                            <span class="place-price">${(place.price).toLocaleString()}원</span>
                        </div>
                    </div>
                </div>
            `;
        });

        return {html, isLast};
    }

    return {
        placeListLayout: placeListLayout,
    }
})();