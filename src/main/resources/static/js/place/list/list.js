import {slicePlaceListComponents} from '../../components/place/placeComponents.js';
import {districtFilter, setAreaAndCallback} from '../../global-js/list-filter-modal.js';
import {addSlideEvent} from '../../global-js/image-slide.js';
import {bookmarkFetch} from '../../modules/fetch/bookmarkFetch.js';
import {loadingMarkService} from "../../modules/loadingMark.js";

// 무한 페이징
let isLoading;
let page = 1;
let isLastPage;

const loadingMark = document.getElementById('loadingMark');

const searchPageInput = document.getElementById('searchPageInput');

const selectBoxBtn = document.querySelector('.SelectBoxBtn');
const selectBoxList = document.querySelector('.SelectBoxList');
const selectBoxBtnImg = document.querySelector('.SelectBoxBtnImg');

const sortSelectBoxBtn = document.querySelector('#sortType .SelectBoxBtn');
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

// 좋아요 버튼
$(`.ListItemsContainer`).on('click', '.ItemBookMarkBtn', function () {
    let isLoggedIn = $('#isLoggedIn').val();
    if (isLoggedIn === 'false') {
        alert('로그인이 필요한 서비스 입니다');
        location.href = '/user/login';
        return;
    }
    const placeId = $(this).data('placeid');
    const status = $(this).data('status');

    bookmarkFetch(status, placeId)
        .then((boo) => {
            $(this).data('status', boo);
        });

    $(this).find('span').toggleClass('none');
});

async function reloadPage() {
    const {district, detailDistrict} = districtFilter;
    isLoading = true;
    let htmlC = ``;

    await loadingMarkService.show(loadingMark);

    const {html, isLast} = await slicePlaceListComponents(0, sortInput.value, district, detailDistrict, searchPageInput.value);

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

    const {html, isLast} = await slicePlaceListComponents(page, sortInput.value, district, detailDistrict, searchPageInput.value);

    listItemsContainer.insertAdjacentHTML("beforeend", html);
    isLastPage = isLast;

    await loadingMarkService.hide(loadingMark);

    await addSlideEvent();
    page++;
    isLoading = false;
}

searchPageInput.addEventListener('keyup', (e) => {
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