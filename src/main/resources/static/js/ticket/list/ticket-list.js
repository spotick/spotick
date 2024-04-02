import {requestLike} from "../../modules/likeFetch.js";
import {showTicketListEvent} from "../../components/ticket/ticketComponent.js";
import {loadingMarkService} from "../../modules/loadingMark.js";

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

const districtInput = document.getElementById('district');
const detailDistrictInput = document.getElementById('detailDistrict');

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

let isLoggedIn = document.getElementById('isLoggedIn').value;
let page = 1;
let isLoading = false;
let isLastPage = document.getElementById("next").value;

const postContainer = document.getElementById('postContainer');
const loadingMark = document.getElementById('loadingMark');

// 이벤트 위임으로 좋아요 기능 구현
postContainer.addEventListener('click', function (e) {
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
            this.setAttribute('data-status', result);

            changeLike(itemLikeBtn, result);
        });
    }
});

districtInput.addEventListener('change', () => {
    reloadPage();
})

// 스크롤시 슬라이스 로딩
window.addEventListener('scroll', function () {
    if (isLoading === true || isLastPage === true) return;

    let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

    if (clientHeight + scrollTop >= scrollHeight) {
        loadNextPage();
    }
});

function loadNextPage() {
    isLoading = true;
    loadingMarkService.show(loadingMark)
        .then(() => {
            window.scrollTo({
                top: document.body.scrollHeight,
                behavior: 'smooth'
            });
            return showTicketListEvent(page, categoryInput.value, rateInput.value, sortInput.value, districtInput.value, detailDistrictInput.value, postContainer);
        })
        .then(() => {
            loadingMarkService.hide(loadingMark);
            page++;
            isLoading = false;
        })
        .catch(error => {
            console.error("로드 중 오류 발생 : ", error);
            isLoading = false;
        });
}

function reloadPage() {
    postContainer.innerHTML = '';
    page = 0;

    isLoading = true;
    loadingMarkService.show(loadingMark)
        .then(() => {
            window.scrollTo({
                top: document.body.scrollHeight,
                behavior: 'smooth'
            });
            return showTicketListEvent(page, categoryInput.value, rateInput.value, sortInput.value, districtInput.value, detailDistrictInput.value, postContainer);
        })
        .then(() => {
            loadingMarkService.hide(loadingMark);
            page++;
            isLoading = false;
        })
        .catch(error => {
            console.error("로드 중 오류 발생 : ", error);
            isLoading = false;
        });
}
