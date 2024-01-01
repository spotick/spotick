function popupInquiryModal() {
    openModal(modalInquiry)
}

function showGSForInquiryDeletion(inquiryId) {
    showGlobalSelection("문의 내역을 삭제하시겠습니까?", () => deleteInquiry(inquiryId))
}

function deleteInquiry(inquiryId) {
    console.log(inquiryId)
}