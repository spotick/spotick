import {slicePlaceListComponents} from '../../modules/place/placeComponents.js';
import {districtFilter, setAreaAndCallback} from '../../global-js/list-filter-modal.js';
import {addSlideEvent} from '../../global-js/image-slide.js';
import {bookmarkFetch} from '../../modules/fetch/bookmarkFetch.js';
import {loadingMarkService} from "../../utils/loadingMark.js";

// 무한 페이징
let isLoading;
let page = 1;
let isLastPage;

const isLoggedIn = document.getElementById('isLoggedIn').value;

const loadingMark = document.getElementById('loadingMark');

const searchInput = document.getElementById('searchInput');

const selectBoxBtn = document.querySelector('.SelectBoxBtn');
const selectBoxList = document.querySelector('.SelectBoxList');
const selectBoxBtnImg = document.querySelector('.SelectBoxBtnImg');

const sortSelectBoxList = document.querySelector('#sortType .SelectBoxList');
const sortSelectBoxBtnImg = document.querySelector('#sortType .SelectBoxBtnImg');
const sortSelectBoxBtnText = document.querySelector('#sortType .SelectBoxBtnText');
const sortListItems = document.querySelectorAll('#sortType .SelectBoxListItem');

const sortInput = document.getElementById('sortInput');

const contentsContainer = document.getElementById('contentsContainer');

// 인기순 필터
selectBoxBtn.addEventListener('click', function () {
    // 토글 기능을 이용하여 리스트 보이기/숨기기
    selectBoxList.style.display = (selectBoxList.style.display === 'block') ? 'none' : 'block';
    // 이미지 변경
    selectBoxBtnImg.src = (selectBoxList.style.display === 'block') ? '/imgs/arrow_up_gray014.75d8599e.svg' : '/imgs/arrow_down_gray014.f502da9d.svg';
});

sortListItems.forEach(item => {
    item.addEventListener('click', function () {
        sortListItems.forEach(otherItem => {
            otherItem.classList.remove('SelectBoxListItem-select');
        });
        this.classList.add('SelectBoxListItem-select');
        sortSelectBoxBtnText.textContent = this.textContent;
        sortSelectBoxList.style.display = 'none';
        sortSelectBoxBtnImg.src = '/imgs/arrow_down_gray014.f502da9d.svg';
        sortInput.value = this.getAttribute("sortType");
        reloadPage();
    });
});

// 북마킹
contentsContainer.addEventListener('click', (e) => {
    const bookmarkBtn = e.target.closest(".ItemBookMarkBtn");

    if (bookmarkBtn) {
        if (isLoggedIn === 'false') {
            alert('로그인이 필요한 서비스 입니다');
            location.href = '/user/login';
            return;
        }
        const placeId = bookmarkBtn.getAttribute('data-placeid');
        const status = bookmarkBtn.getAttribute('data-status');

        bookmarkFetch(status, placeId, (r) => {
            bookmarkBtn.setAttribute('data-status', r);

            changeLike(bookmarkBtn, r);
        });
    }
});

function changeLike(btn, status) {
    // status에 따라서 클래스 변경
    const off = btn.children[0];
    const on = btn.children[1];

    if (status) {
        off.classList.add('none');
        on.classList.remove('none')
    } else {
        off.classList.remove('none');
        on.classList.add('none')
    }
}

async function reloadPage() {
    const {district, detailDistrict} = districtFilter;
    isLoading = true;
    let htmlC;

    await loadingMarkService.show(loadingMark);

    const {
        html,
        isLast
    } = await slicePlaceListComponents(0, sortInput.value, district, detailDistrict, searchInput.value);

    if (!html) {
        htmlC = `
            <div class="empty-list-content">
                <img src="/imgs/empty.png" alt="empty">
                <p class="main mt-4">
                    일치하는 결과가 없어요
                </p>
                <p class="sub mt-1">
                    검색 범위를 넓혀 보세요.
                </p>
            </div>
        `;
    } else {
        htmlC = `
            <div class="ListItemsContainer">
                ${html}
            </div>
        `;
    }

    contentsContainer.innerHTML = htmlC;
    isLastPage = isLast;

    await loadingMarkService.hide(loadingMark);

    await addSlideEvent();
    page = 1;
    isLoading = false;
}

/*
* 조건의 변화가 없고 더 많은 컨텐츠를 가져와야할 시 사용할 함수
* */
async function getMoreContents() {
    const listItemsContainer = contentsContainer.querySelector('.ListItemsContainer');
    const {district, detailDistrict} = districtFilter;
    isLoading = true;

    await loadingMarkService.show(loadingMark);

    window.scrollTo({
        top: document.body.scrollHeight,
        behavior: 'smooth'
    });

    const {
        html,
        isLast
    } = await slicePlaceListComponents(page, sortInput.value, district, detailDistrict, searchInput.value);

    listItemsContainer.insertAdjacentHTML("beforeend", html);
    isLastPage = isLast;

    await loadingMarkService.hide(loadingMark);

    await addSlideEvent();
    page++;
    isLoading = false;
}

searchInput.addEventListener('keyup', (e) => {
    if (e.key === 'Enter') {
        reloadPage();
    }
});

window.addEventListener('scroll', function () {
    if (isLoading === true || isLastPage === true) return;

    let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

    if (clientHeight + scrollTop >= scrollHeight) {
        getMoreContents();
    }
});

document.getElementById('filterSubmitBtn').addEventListener('click', () => {
    setAreaAndCallback(reloadPage);
});