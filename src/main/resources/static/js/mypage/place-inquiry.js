import {loadingMarkService} from '../utils/loadingMark.js';
import {slicePlaceInquiryListHostComponent} from "../modules/inquiry/InquiryComponent.js";
import {modalLayouts} from "../layouts/mypage/modalLayouts.js";
import {closeSingleModal, showCustomModal, showGlobalDialogue, showGlobalSelection} from "../global-js/global-modal.js";
import {inquiryService} from "../services/inquiry/inquiryService.js";

const placeModal = document.getElementById('placeModal');
const inquiriesContainer = document.getElementById('inquiriesContainer');
const loadingMark = document.getElementById('mpLoadingMark');

let page = 0;
let isLastPage = false;
let isLoading = false;
let contentsSaver = [];

window.onload = () => {
    // 첫 리스트 출력
    getInquiryList();

    // 이후 리스트 출력
    window.addEventListener('scroll', function () {
        if (isLoading || isLastPage) return;

        let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

        if (clientHeight + scrollTop >= scrollHeight) {
            getInquiryList();
        }
    });
};


const getInquiryList = async () => {
    if (!isLastPage && !isLoading) {
        isLoading = true;
        await loadingMarkService.show(loadingMark);

        const {isLast, html, contents} = await slicePlaceInquiryListHostComponent(pId, page);

        contentsSaver = contentsSaver.concat(contents);
        isLastPage = isLast;

        if (html === '') {
            loadNoList();
        }

        inquiriesContainer.insertAdjacentHTML("beforeend", html);

        loadingMarkService.hide(loadingMark);
        isLoading = false;
        page++;
    }
}

function loadNoList() {
    inquiriesContainer.innerHTML =
        `<div class="mpcp-no-list">
            <span>문의가 없습니다.</span>
         </div>`;
}

function generateEventListenerOnModal() {
    const responseTxArea = placeModal.querySelector('#response');
    const requestButton = placeModal.querySelector('#requestBtn');
    const typeCounter = placeModal.querySelector('#typeCounter');

    responseTxArea.addEventListener('input', () => {
        const maxCharCount = 200;
        if (responseTxArea.value.length > maxCharCount) {
            responseTxArea.value = responseTxArea.value.slice(0, maxCharCount);
        }
        typeCounter.textContent = `${responseTxArea.value.length}`;

        requestButton.disabled = responseTxArea.value.length < 10;
    });

    requestButton.addEventListener('click', () => {
        const inquiryId = parseInt(requestButton.getAttribute('iId'));
        showGlobalSelection(
            "답변을 등록하시겠습니까?",
            () => responseInquiry(pId, inquiryId, responseTxArea.value),
            null,
            false
        );
    });
}

const responseInquiry = async (placeId, inquiryId, response) => {
    const {success, message} = await inquiryService.responsePlaceInquiry(placeId, inquiryId, response);

    if (success) {
        const inquiries = inquiriesContainer.querySelectorAll('.inquiry');
        if (inquiries.length === 1) {
            loadNoList();
        }

        inquiries.forEach(inquiry => {
            const iId = parseInt(inquiry.getAttribute('iId'));
            if (iId === inquiryId) {
                inquiry.remove();
            }
        });

        contentsSaver = contentsSaver.filter(content => content.id !== inquiryId);

        showGlobalDialogue(message);
    } else {
        const errorLine = placeModal.querySelector('#errorContent');
        closeSingleModal("gs");
        errorLine.innerHTML = message;
    }
}


inquiriesContainer.addEventListener('click', (e) => {
    const button = e.target.closest('.detailOpen');
    if (button) {
        const inquiryId = parseInt(button.getAttribute('iId'));

        contentsSaver.forEach(content => {
            if (content.id === inquiryId) {
                placeModal.innerHTML = modalLayouts.inquiryRequestModalLayout(content);
                generateEventListenerOnModal();

                showCustomModal(placeModal);
            }
        });
    }
});