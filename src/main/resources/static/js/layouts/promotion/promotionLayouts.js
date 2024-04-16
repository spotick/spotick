export const promotionLayouts = (() => {

    const promotionListLayout = () => {

    }

    const promotionMoreContentsLayout = (data) => {
        let html = ``;
        let hasMoreContents = !data.last;

        html += `<div class="flex-wrap">`;

        data.content.forEach(({promotionId, title, thumbnailImage}) => {
            html += `
                <a href="/promotion/${promotionId}" class="swiper-slide">
                    <div class="CategoryContentItem">
                        <img alt="${title}"
                             class="CategoryContentItemImage"
                             src="/file/display?fileName=${thumbnailImage.uploadPath}/t_${thumbnailImage.uuid}_${thumbnailImage.fileName}">
                        <p>${title}</p>
                    </div>
                </a>
            `;
        });

        html += `</div>`

        return {html, hasMoreContents};
    }

    const loadMoreBtn = () => {
        return `
            <div class="CategoryContentsBtnContainer" id="moreContents">
                <button class="Cleia OtherBtn" type="button">다른 컨텐츠 더보기</button>
            </div> 
        `;
    }

    return {
        promotionListLayoutL: promotionListLayout,
        promotionMoreContentsLayout: promotionMoreContentsLayout,
        loadMoreBtn: loadMoreBtn,
    }
})();