export const bookmarkFetch = async (status, placeId, callback) => {
    try {
        const response = await fetch(`/bookmark?status=${status}&placeId=${placeId}`, {method: 'GET'});
        const result = await response.json();

        if (callback) {
            callback(result);
        }
    } catch (e) {
        console.error("리퀘스트 오류 발생:", e);
    }
}