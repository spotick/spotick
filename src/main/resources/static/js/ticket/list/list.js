import {sliceTicketListComponent} from "../../components/ticket/ticketComponent.js";
import {requestLike} from "../../modules/fetch/likeFetch.js";
import {districtFilter, setAreaAndCallback} from '../../global-js/list-filter-modal.js';
import {loadingMarkService} from "../../modules/loadingMark.js";

let isLoading = false;
let page = 1;
let isLastPage = false;

const isLoggedIn = document.getElementById('isLoggedIn').value;

// ============================================== 선언부
const categoryInput = document.getElementById('category');
const sortInput = document.getElementById('sort');
const rateInput = document.getElementById('rate')

const sortSelectBoxBtn = document.querySelector('#sortType .SelectBoxBtn');
const sortSelectBoxList = document.querySelector('#sortType .SelectBoxList');
const sortSelectBoxBtnImg = document.querySelector('#sortType .SelectBoxBtnImg');
const sortSelectBoxBtnText = document.querySelector('#sortType .SelectBoxBtnText');
const sortListItems = document.querySelectorAll('#sortType .SelectBoxListItem');

const ratingSelectBoxBtn = document.querySelector('#ratingType .SelectBoxBtn');
const ratingSelectBoxList = document.querySelector('#ratingType .SelectBoxList');
const ratingSelectBoxBtnImg = document.querySelector('#ratingType .SelectBoxBtnImg');
const ratingSelectBoxBtnText = document.querySelector('#ratingType .SelectBoxBtnText');
const ratingListItems = document.querySelectorAll('#ratingType .SelectBoxListItem');

const searchInput = document.getElementById('searchInput');

// 인기순 필터
sortSelectBoxBtn.addEventListener('click', function () {
    sortSelectBoxList.style.display = (sortSelectBoxList.style.display === 'block') ? 'none' : 'block';
    sortSelectBoxBtnImg.src = (sortSelectBoxList.style.display === 'block') ? '/imgs/arrow_up_gray014.75d8599e.svg' : '/imgs/arrow_down_gray014.f502da9d.svg';
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

// 관람 등급 필터
ratingSelectBoxBtn.addEventListener('click', function () {
    ratingSelectBoxList.style.display = (ratingSelectBoxList.style.display === 'block') ? 'none' : 'block';
    ratingSelectBoxBtnImg.src = (ratingSelectBoxList.style.display === 'block') ? '/imgs/arrow_up_gray014.75d8599e.svg' : '/imgs/arrow_down_gray014.f502da9d.svg';
});

ratingListItems.forEach(item => {
    item.addEventListener('click', function () {
        ratingListItems.forEach(otherItem => {
            otherItem.classList.remove('SelectBoxListItem-select');
        });
        this.classList.add('SelectBoxListItem-select');
        ratingSelectBoxBtnText.textContent = this.textContent;
        ratingSelectBoxList.style.display = 'none';
        ratingSelectBoxBtnImg.src = '/imgs/arrow_down_gray014.f502da9d.svg';
        rateInput.value = this.getAttribute("ratingType");
        reloadPage();
    });
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

const categories = document.querySelectorAll('.swiper-slide');

categories.forEach((category) => {
    category.addEventListener('click', () => {
        categories.forEach(item => item.classList.remove('active'));
        category.classList.add('active');

        categoryInput.value = category.getAttribute('category')

        reloadPage();
    });
});

/////////////////////////////////////////////////////////////////////////////////////////
HTMLCollection.prototype.forEach = Array.prototype.forEach;

const contentsContainer = document.getElementById('contentsContainer');
const loadingMark = document.getElementById('loadingMark');

// 이벤트 위임으로 좋아요 기능 구현
contentsContainer.addEventListener('click', function (e) {
    const itemLikeBtn = e.target.closest(".ItemLikeBtn");
    if (itemLikeBtn) {
        if (isLoggedIn === 'false') {
            alert('로그인이 필요한 서비스 입니다');
            location.href = '/user/login';
            return;
        }

        const ticketId = itemLikeBtn.getAttribute("data-id");
        const status = itemLikeBtn.getAttribute("data-status");

        requestLike(status, ticketId, (result) => {
            itemLikeBtn.setAttribute('data-status', result);

            changeLike(itemLikeBtn, result);
        });
    }
});

// 스크롤시 슬라이스 로딩
window.addEventListener('scroll', function () {
    if (isLoading === true || isLastPage === true) return;

    let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

    if (clientHeight + scrollTop >= scrollHeight) {
        getMoreContents();
    }
});

async function reloadPage() {
    const {district, detailDistrict} = districtFilter;
    isLoading = true;
    let htmlC = ``;

    await loadingMarkService.show(loadingMark);

    const {
        html,
        isLast
    } = await sliceTicketListComponent(0, categoryInput.value, rateInput.value, sortInput.value, district, detailDistrict, searchInput.value);

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

    page = 1;
    isLoading = false;
}

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
    } = await sliceTicketListComponent(page, categoryInput.value, rateInput.value, sortInput.value, district, detailDistrict, searchInput.value);

    listItemsContainer.insertAdjacentHTML("beforeend", html);
    isLastPage = isLast;

    await loadingMarkService.hide(loadingMark);

    page++;
    isLoading = false;
}

searchInput.addEventListener('keyup', (e) => {
    if (e.key === 'Enter') {
        reloadPage();
    }
});

document.getElementById('filterSubmitBtn').addEventListener('click', () => {
    setAreaAndCallback(reloadPage);
});
