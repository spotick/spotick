import {loadingMarkService} from '../utils/loadingMark.js';
import {placeInquiriesPaginationComponent, ticketInquiriesPaginationComponent} from "../modules/mypage/InquiryComponent.js";
import {showGlobalSelection, showGlobalDialogue, closeEveryModal, showCustomModal} from "../global-js/global-modal.js";
import {inquiryService} from "../services/inquiry/inquiryService.js";
import {modalLayouts} from "../layouts/mypage/modalLayouts.js";

const loadingMark = document.getElementById('loadingMark');
const contentsContainer = document.getElementById('inquiryContainer');
const paginationContainer = document.getElementById('paginationContainer');
const inquiryModal = document.getElementById('inquiryModal');

let type;
let inquiriesContent = [];

document.getElementById('place').addEventListener('click', () => {
    getPlaceInquiriesPage(1);
});

document.getElementById('ticket').addEventListener('click', () => {
    getTicketInquiriesPage(1);
});

paginationContainer.addEventListener('click', (e) => {
    const pageButton = e.target.closest('.pagination-btns');

    if (pageButton) {
        const page = pageButton.getAttribute('page');

        switch (type) {
            case "place":
                getPlaceInquiriesPage(page);
                break;
            case "ticket":
                getTicketInquiriesPage(page);
                break;
        }
    }
});

contentsContainer.addEventListener('click', async (e) => {
    const reservationDelete = e.target.closest('.reservationDelete');
    const inquiryDetail = e.target.closest('.inquiryDetail');

    if (reservationDelete) {
        const inquiryId = reservationDelete.getAttribute('id');

        showGlobalSelection("문의 내역을 삭제하시겠습니까?", () => {
            switch (type) {
                case "place":
                    deletePlaceInquiry(inquiryId);
                    break;
                case "ticket":
                    deleteTicketInquiry(inquiryId);
                    break;
            }
        });
    }

    if (inquiryDetail) {
        const index = inquiryDetail.getAttribute('idx');

        inquiryModal.innerHTML = await modalLayouts.inquiryDetailModalLayout(inquiriesContent[index]);

        showCustomModal(inquiryModal);
    }
});

const getPlaceInquiriesPage = async (page) => {
    await loadingMarkService.show(loadingMark);
    const {inquiries, contentHtml, paginationHtml} = await placeInquiriesPaginationComponent(page);

    inquiriesContent = inquiries.map(item => ({
        content: item.content,
        response: item.response,
        responded: item.responded
    }));

    loadingMarkService.hide(loadingMark);
    if (contentHtml === "") {
        loadNoList();
        return;
    }
    contentsContainer.innerHTML = contentHtml;
    paginationContainer.innerHTML = paginationHtml;
    type = "place";
}

const getTicketInquiriesPage = async (page) => {
    await loadingMarkService.show(loadingMark);
    const {inquiries, contentHtml, paginationHtml} = await ticketInquiriesPaginationComponent(page);

    inquiriesContent = inquiries.map(item => ({
        content: item.content,
        response: item.response,
        responded: item.responded
    }));

    loadingMarkService.hide(loadingMark);
    if (contentHtml === "") {
        loadNoList();
        return;
    }
    contentsContainer.innerHTML = contentHtml;
    paginationContainer.innerHTML = paginationHtml;
    type = "ticket"
}

function loadNoList() {
    contentsContainer.innerHTML =
        `<div class="mpcp-no-list">
            <span>문의 내역이 없습니다.</span>
         </div>`;
    paginationContainer.innerHTML = "";
}

const deletePlaceInquiry = async (inquiryId) => {
    const {success, message, data} = await inquiryService.deletePlaceInquiry(inquiryId);

    if (success) {
        showGlobalDialogue(message, () => {
            contentsContainer.querySelectorAll('article').forEach(article => {
                if (article.getAttribute('id') === data.toString()) {
                    article.remove();
                    closeEveryModal()
                }
            });
        });
    } else {
        showGlobalDialogue(message);
    }
}

const deleteTicketInquiry = async (inquiryId) => {
    const {success, message, data} = await inquiryService.deleteTicketInquiry(inquiryId);

    if (success) {
        showGlobalDialogue(message, () => {
            contentsContainer.querySelectorAll('article').forEach(article => {
                if (article.getAttribute('id') === data.toString()) {
                    article.remove();
                    closeEveryModal()
                }
            });
        });
    } else {
        showGlobalDialogue(message);
    }
}