// 북마크 버튼 컨트롤
const bookmarks = document.querySelectorAll('.mpccc-bookmark');

bookmarks.forEach(bookmark => {
    bookmark.addEventListener('click', () => {
        bookmark.classList.toggle('active');
    })
})