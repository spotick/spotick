const content = document.getElementById('detailContent');
const response = document.getElementById('detailResponse');


let currentPage = 0;

function popupInquiryModal(item) {
    openModal(modalInquiry)

    content.value = item.getAttribute('data-content');

    const responseString = item.getAttribute('data-response');

    if (responseString === "null") {
        response.value = "";
    } else {
        response.value = responseString;
    }
}

function showGSForInquiryDeletion(inquiryId) {
    showGlobalSelection("문의 내역을 삭제하시겠습니까?", () => deleteInquiry(inquiryId))
}

function deleteInquiry(inquiryId) {
    fetch(`/inquiries/${inquiryId}/delete`, {
        method: 'GET'
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw response
            }
        })
        .then(message => {
            alert(message);
            closeModal();
            inquiryService.requestPlaceInquiries(currentPage, inquiryService.reloadPlaceInquiries);
        })
        .catch(error => {
            console.error('Error:', error);

            error.text().then(message => showGlobalDialogue(message))
        });
}

const container = document.getElementById('inquiryContainer')
const paginationContainer = document.querySelector('.pagination-container')
const inquiryService = (function () {

    function requestPlaceInquiries(page, callback) {
        container.innerHTML = '';
        loadingMarkService.show();

        // todo : 제거 -> 로딩 스크린 테스트 용
        setTimeout(() => {

        fetch('/inquiries/place?page=' + page, {
            method: 'GET'
        })
            .then(response => {
                loadingMarkService.hide();
                if (response.ok) {
                    return response.json();
                } else {
                    throw response;
                }
            })
            .then(data => {
                callback(data);
                currentPage = page;
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }, 500)
    }

    function reloadPlaceInquiries(data) {

        console.log(data)

        container.innerHTML = '';

        data.data.content.forEach(inquiry => {
            let articleHtml =
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
                                    <div class="action-item" onclick="showGSForInquiryDeletion(${inquiry.id})">
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
                        <div class="mpcr-info" onclick="popupInquiryModal(this)" 
                            data-content="${inquiry.content}" data-response="${inquiry.response}">
                            <div class="mpcri-title">문의 내용</div>
                            <div class="mpcriq-title">${inquiry.inquiryTitle}</div>
                            <div class="mpcriq-content">${inquiry.content}</div>
                        </div>
                    </div>
                </article>`

            container.insertAdjacentHTML("beforeend", articleHtml);
        })

        paginationReload(data)
    }

    function paginationReload(data) {
        let paginationHtml = "";

        if (data.pagination.hasPrevBlock) {
            paginationHtml +=
                `<span class="pagination-previous">
                    <a class="pagination-btns first" onclick="inquiryService.requestPlaceInquiries(1, inquiryService.reloadPlaceInquiries), scrollToTop()"
                        title="맨 처음">
                        <i class="fa-solid fa-chevron-left"></i>
                        <i class="fa-solid fa-chevron-left"></i>
                    </a>
                    <a class="pagination-btns"
                        onclick="inquiryService.requestPlaceInquiries(${data.pagination.startPage - 1}, inquiryService.reloadPlaceInquiries), scrollToTop()"
                        title="이전">
                        <i class="fa-solid fa-chevron-left"></i>
                    </a>
                </span>`
        }

        for (let i = data.pagination.startPage; i <= data.pagination.endPage; i++) {
            paginationHtml += "<span class=\"pagination-body\">";

            paginationHtml +=
                `<span class="pagination-btns">
                    <a class="${i === data.pagination.currentPage ? 'active' : ''}"
                       onclick="inquiryService.requestPlaceInquiries(${i}, inquiryService.reloadPlaceInquiries), scrollToTop()">
                       ${i}
                    </a>
                </span>`

            paginationHtml += "</span>";
        }

        if (data.pagination.hasNextBlock) {
            paginationHtml +=
                `<span class="pagination-next">
                    <a class="pagination-btns"
                        onclick="inquiryService.requestPlaceInquiries(${data.pagination.endPage + 1}, inquiryService.reloadPlaceInquiries), scrollToTop()"
                        title="다음">
                        <i class="fa-solid fa-chevron-right"></i>
                    </a>
                    <a class="pagination-btns end" onclick="inquiryService.requestPlaceInquiries(${data.pagination.lastPage}, inquiryService.reloadPlaceInquiries), scrollToTop()"
                       title="맨 끝">
                        <i class="fa-solid fa-chevron-right"></i>
                        <i class="fa-solid fa-chevron-right"></i>
                    </a>
                </span>`;
        }

        paginationContainer.innerHTML = paginationHtml;
    }

    return {
        requestPlaceInquiries: requestPlaceInquiries,
        reloadPlaceInquiries: reloadPlaceInquiries
    }
})();