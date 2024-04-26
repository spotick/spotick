import {slicePlaceListComponents} from "../components/place/placeComponents.js";
import {loadingMarkService} from "../modules/loadingMark.js";
import {districtFilter} from '../ticket/list/list-filter-modal.js';

let isLoading;
let page = 0;
let isLastPage;

const loadingMark = document.getElementById('loadingMark');
const searchPageInput = document.getElementById('searchPageInput');
const contentsContainer = document.getElementById('contentsContainer');

const sortType = document.getElementById('sortType');

const districtInput = document.getElementById('district');
const detailDistrictInput = document.getElementById('detailDistrict');

/*
* 조건 추가 및 변경으로 인해 새로 페이지를 로드 해야할 시 사용할 함수
* */
async function search() {
    const {district, detailDistrict} = districtFilter;
    isLoading = true;
    let htmlC = ``;

    await loadingMarkService.show(loadingMark);

    const {html, isLast} = await slicePlaceListComponents(0, sortType.value, district, detailDistrict, searchPageInput.value);

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

/*
* 조건의 변화가 없고 더 많은 컨텐츠를 가져와야할 시 사용할 함수
* */
async function searchMore() {
    const listItemsContainer = contentsContainer.querySelector('.ListItemsContainer');
    const {district, detailDistrict} = districtFilter;
    isLoading = true;

    await loadingMarkService.show(loadingMark);

    const {html, isLast} = await slicePlaceListComponents(page, sortType.value, district, detailDistrict, searchPageInput.value);

    listItemsContainer.insertAdjacentHTML("beforeend", html);
    isLastPage = isLast;

    await loadingMarkService.hide(loadingMark);

    page++;
    isLoading = false;
}

searchPageInput.addEventListener('keyup', (e) => {
    if (e.key === 'Enter') {
        search();
    }
});

sortType.addEventListener('change', search);
districtInput.addEventListener('change', search);

window.onload = () => {
    search();
}

window.addEventListener('scroll', function () {
    if (isLoading === true || isLastPage === true) return;

    let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

    if (clientHeight + scrollTop >= scrollHeight) {
        searchMore();
    }
});

