import {bookmarkFetch} from "../modules/fetch/bookmarkFetch.js";

const bookmarkButtons = document.querySelectorAll('.ItemBookMarkBtn');

bookmarkButtons.forEach(btn => {

    btn.addEventListener('click', () => {
        toggleBookmark(btn);
    });
})

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


//
const selectButton = document.querySelector('.select-box-btn');
const selectButtonImg = selectButton.querySelector('img');
const selectButtonText = selectButton.querySelector('span');
const selectBoxList = document.querySelector('.select-box-list');

const selectBoxItems = document.querySelectorAll('.select-box-list button');

selectButton.addEventListener('click', () => {
    selectBoxList.classList.toggle('show');
    transformSelectBoxImg(selectBoxList.classList.contains('show'));
});

selectBoxItems.forEach(item => {
    item.addEventListener('click', () => {
        const sortType = item.getAttribute('sortType');

        window.location.href = `/mypage/bookmarks?page=${currentPage}&sort=${sortType}`;
    });
});

const transformSelectBoxImg = (boo) => {
    boo ? selectButtonImg.style.transform = 'rotate(180deg)' : selectButtonImg.style.transform = 'rotate(0deg)';
}
