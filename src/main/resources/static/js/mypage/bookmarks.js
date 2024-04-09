import {bookmarkFetch} from "../modules/fetch/bookmarkFetch.js";

const bookmarkBtns = document.querySelectorAll('.ItemBookMarkBtn');

// 북마크 버튼 컨트롤
function toggleBookmark(btn) {
    const placeId = btn.getAttribute('data-id');
    const status = btn.getAttribute('data-status');

    bookmarkFetch(status, placeId)
        .then(boo => {
            btn.setAttribute('data-status', boo);
            const off = btn.children[0];
            const on = btn.children[1];

            if (boo) {
                off.classList.add('none');
                on.classList.remove('none')
            } else {
                off.classList.remove('none');
                on.classList.add('none')
            }
        });
}

/////////////////////////////////////////////////////
bookmarkBtns.forEach(btn => {

    btn.addEventListener('click', () => {
        toggleBookmark(btn);
    });
})