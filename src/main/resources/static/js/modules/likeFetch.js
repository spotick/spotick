export async function requestLike(status, ticketId, callback) {
    try {
        const response = await fetch(`/like?status=${status}&ticketId=${ticketId}`);
        const result = await response.json();

        if (callback) {
            callback(result);
        }
    } catch (error) {
        console.error("리퀘스트 오류 발생:", error);
    }
}