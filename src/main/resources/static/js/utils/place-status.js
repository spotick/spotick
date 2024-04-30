export const getPlaceStatusName = (status) => {
    switch(status) {
        case "PENDING":
            return "승인 대기중";
        case "WAITING_PAYMENT":
            return "결제 대기 중";
        case "APPROVED":
            return "승인됨";
        case "REJECTED":
            return "거절됨";
        case "CANCELLED":
            return "취소";
        case "COMPLETED":
            return "이용 완료";
        case "DELETED":
            return "삭제됨";
        default:
            return "알 수 없음";
    }
}
