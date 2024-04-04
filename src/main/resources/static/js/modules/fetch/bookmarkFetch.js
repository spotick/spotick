export const bookmarkFetch = async (status, placeId) => {
    const response = await fetch(`/bookmark?status=${status}&placeId=${placeId}`, {method: 'GET'});

    return response.json();
}