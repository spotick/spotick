let page = 0;
let hasNext = true;
let isLoading = false;


const currentPath = window.location.pathname;
const pathSegments = currentPath.split('/');
const placeId = pathSegments[pathSegments.length - 1];

const img = document.getElementById('detailProfileImg');
const nickname = document.getElementById('detailNickName');
const contentArea = document.getElementById('detailContent');

const inquiryIdInput = document.getElementById('inquiryId');
const responseInput = document.getElementById('response');

const errorContent = document.querySelector('.error-content');

const inquiryContainer = document.getElementById('inquiryContainer');

const inquiryService = (function () {

    function requestInquiries(callback) {
        loadingMarkService.show();

        const placeId = extractVariableFromURL();

        //todo : 로딩 마스크 테스트용, 테스트 후 삭제 필요
        setTimeout(() => {

            fetch(`/inquiries/api/getTicket/${placeId}?page=${page}`, {
                method: 'GET'
            })
                .then(response => {
                    loadingMarkService.hide();
                    if (response.status === 204) {
                        return null;
                    } else if (response.status === 200) {
                        return response.json();
                    } else {
                        throw response;
                    }
                })
                .then(response => {
                    if (response && response.data && response.data.content) {
                        if (response.data.last) {
                            hasNext = false;
                        }
                        callback(response.data.content);
                    } else {
                        hasNext = false;
                        loadNoList();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                })
                .finally(() => {
                    isLoading = false;
                })

        }, 500);
    }

    function loadInquiries(content) {
        content.forEach(inquiry => {

            let html =
                `<div class="mpcp-item">
                    <div class="mpcpi-top">
                        <div class="mpcpi-request-user-con">
                            <div class="mpcpi-ruser-icon">
                                ${inquiry.defaultImage ?
                    `<img src="/file/default/display?fileName=${inquiry.fileName}">` :
                    `<img src="/file/display?fileName=${inquiry.uploadPath}/t_${inquiry.uuid}_${inquiry.fileName}">`
                }
                            </div>
                            <span class="mpcp-ruser-name">${inquiry.nickname}</span>
                        </div>
                    </div>
                    <div class="mpcp-body">
                        <div class="mpcp-content" style="font-size: 18px;">
                            <span>${inquiry.inquiryTitle}</span>
                        </div>
                        <div class="mpcp-btn detailInquiryBtn"
                             data-content="${inquiry.content}"
                             data-id="${inquiry.id}"
                             data-nickname="${inquiry.nickname}"
                             data-title="${inquiry.inquiryTitle}"
                             ${inquiry.defaultImage ?
                    `data-img="/file/default/display?fileName=${inquiry.fileName}">` :
                    `data-img="/file/display?fileName=${inquiry.uploadPath}/t_${inquiry.uuid}_${inquiry.fileName}">`
                }
                            <span>상세보기</span>
                        </div>
                    </div>
                </div>`;

            inquiryContainer.insertAdjacentHTML("beforeend", html);
        });

        document.querySelectorAll('.detailInquiryBtn').forEach(inquiryBtn => {
            inquiryBtn.addEventListener('click', function () {
                img.src = this.getAttribute('data-img');
                nickname.innerHTML = this.getAttribute('data-nickname');
                contentArea.value = this.getAttribute('data-content');
                inquiryIdInput.value = this.getAttribute('data-id');

                responseInput.value = '';
                errorContent.innerHTML = '';

                openModal(modalPlace);
            });
        });
    }

    function requestUploadResponse(ticketId, inquiryId, responseString) {
        closeOnlyThisModal(globalSelection);

        const inquiryResponse = {
            id: ticketId,
            inquiryId: inquiryId,
            response: responseString
        }

        fetch('/inquiries/api/responseTicketInquiry', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(inquiryResponse)
        })
            .then(response => {
                if (response.ok) {
                    alert("답변이 등록되었습니다.");
                    window.location.reload();
                } else {
                    throw response
                }
            })
            .catch(error => {
                console.error('Error:', error);

                error.text().then(message => {

                    vibrateTarget(modalPlace);

                    errorContent.innerHTML = message;
                })
            });
    }

    function loadNoList() {
        inquiryContainer.innerHTML =
            `<div class="mpcp-no-list">
            <span>문의 내역이 없습니다.</span>
         </div>`;
    }

    return {
        requestInquiries: requestInquiries,
        loadInquiries: loadInquiries,
        requestUploadResponse: requestUploadResponse
    }
})();

///////////////////////////////////////////////////////////////////////////////

window.onload = function () {
    // 화면 로드
    inquiryService.requestInquiries(inquiryService.loadInquiries);

    // 스크롤 이벤트 리스너
    window.addEventListener('scroll', function () {
        if (isLoading || !hasNext) return;

        let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

        if (clientHeight + scrollTop >= scrollHeight) {
            isLoading = true;
            page++;
            inquiryService.requestInquiries(inquiryService.loadInquiries);
        }
    })
}

document.getElementById('requestBtn').addEventListener('click', function () {
    let inquiryId = inquiryIdInput.value;
    let ticketId = extractVariableFromURL();
    let responseString = responseInput.value;


    showGlobalSelection(
        "답변을 등록하시겠습니까?",
        () => inquiryService.requestUploadResponse(ticketId, inquiryId, responseString)
    );
})