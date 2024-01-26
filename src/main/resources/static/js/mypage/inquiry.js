function popupInquiryModal() {
    openModal(modalInquiry)
}

function showGSForInquiryDeletion(inquiryId) {
    showGlobalSelection("문의 내역을 삭제하시겠습니까?", () => deleteInquiry(inquiryId))
}

function deleteInquiry(inquiryId) {
    console.log(inquiryId)
}

const container = document.getElementById('inquiryContainer')
const inquiryService = (function () {

    function requestPlaceInquiries(page, callback) {
        fetch('/inquiries/place?page=' + page, {
            method: 'GET'
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw response
                }
            })
            .then(data => {
                callback(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function reloadPlaceInquiries(data) {

        console.log(data)

        container.innerHTML = '';

        data.content.forEach(inquiry => {
            let article =
                `<article>
                    <div class="mpcr-item-container">
                        <div class="mpcr-action-btn">
                            <button onclick="toggleDropdown(this)" type="button">
                                <i aria-hidden="true" class="fa-solid fa-ellipsis"></i>
                                <div class="mpc-dropdown">
                                    <div class="action-item">
                                        <a href="/place/detail/${inquiry.id}">해당
                                                    게시글로 이동</a>
                                    </div>
                                    <div class="action-item" onclick="showGSForInquiryDeletion(5)">
                                        <span>문의 내역 삭제</span>
                                    </div>
                                </div>
                            </button>
                        </div>
                        <div class="mpcr-img-con">
                            <img height="100px" 
                                src="/file/display?fileName=${inquiry.placeFileDto.uploadPath}/t_${inquiry.placeFileDto.uuid}_${inquiry.placeFileDto.fileName}">
                                 style="width: 180px;">
                        </div>
                        <div class="ItemTextContainer">
                            <div class="ItemHostNameContainer">
                                <span class="ItemHostName">${inquiry.placeAddress.address}</span>
                                <div class="ItemCountsContainer">
                                    <div class="ItemsStarCountContainer">
                                        <img alt="후기갯수" class="ItemCountImg"
                                             src="/imgs/star_filled_paintYellow056.a8eb6e44.svg">
                                        <span class="ItemCountText">${inquiry.evalAvg.toFixed(1)}(${inquiry.evalCount})</span>
                                    </div>
                                    <div class="ItemsLikeCountContainer">
                                        <img alt="북마크갯수" class="ItemCountImg bookmark"
                                             src="/imgs/bookmark_thin.svg">
                                        <span class="ItemCountText">${inquiry.bookmarkCount}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="ItemSpaceNameContainer">
                                <p class="ItemSpaceName">${inquiry.placeTitle}</p>
                            </div>
                            <div class="ItemPriceContainer">
                                <span class="ItemPrice">${inquiry.price.toLocaleString()}원</span>
                            </div>
                        </div>
                        <div class="mpcr-divider"></div>
                        <div class="mpcr-info" onclick="popupInquiryModal()">
                            <div class="mpcri-title">문의 내용</div>
                            <div class="mpcriq-title">${inquiry.inquiryTitle}</div>
                            <div class="mpcriq-content">${inquiry.content}</div>
                        </div>
                    </div>
                </article>`

            container.insertAdjacentHTML("beforeend", article);
        })

    }

    return {
        requestPlaceInquiries: requestPlaceInquiries,
        reloadPlaceInquiries: reloadPlaceInquiries
    }
})();