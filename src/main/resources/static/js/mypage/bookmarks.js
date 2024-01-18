// 북마크 버튼 컨트롤
function toggleBookmark(button) {
    const placeId = button.getAttribute('data-id');

    if (placeId) {
        fetch(`/bookmark?placeId=${placeId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                // Assuming the server returns a boolean indicating whether the bookmark was added or removed
                if (data) {
                    button.classList.add('active');
                } else {
                    button.classList.remove('active');
                }
            })
            .catch(error => {
                console.error('오류:', error);
            });
    } else {
        console.error('placeId를 찾을 수 없음');
        showGlobalDialogue("오류가 발생했습니다.<br>페이지를 새로고침해주세요.")
    }
}