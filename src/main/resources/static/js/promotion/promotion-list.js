import {promotionListComponent} from "../modules/promotion/promotionComponents.js"
import {loadingMarkService} from "../utils/loadingMark.js";

const loadingMark = document.getElementById("loadingMark");
const contentsContainer = document.getElementById("contentsContainer");

const categoryInput = document.getElementById('category');

const modalSortType = document.getElementById("modalSortType");
const sortType = document.getElementById("sortType");

const searchInput = document.getElementById('searchInput');

let filterBtn = document.querySelector(".FilterBtn button")
let filterModal = document.querySelector(".FilterModal")
let modalCloseBtn = document.querySelector(".FilterModalCloseBtn")
let modalSaveBtn = document.querySelector(".FilterModalFooterSaveBtn")
let modalResetBtn = document.querySelector(".FilterModalFooterResetBtn")
let modalCategoryBtn = document.querySelectorAll(".FilterModalCategoryChild")
let CategoryBtn = document.querySelectorAll(".Category")


filterBtn.addEventListener("click", function () {
    // 모달 보이기
    filterModal.style.display = "block";
    document.body.style.overflow = "hidden";
    // CategoryList Category 중에서 "On" 클래스가 있는지 확인
    const categoryOn = document.querySelector('.CategoryList.Category.On');

    // "On" 클래스가 있는 경우 FilterModalCategoryChild에 "On" 클래스 추가
    if (categoryOn && !categoryOn.classList.contains('CategoryList.Category.All')) {
        const categoryText = categoryOn.textContent.trim();
        document.querySelectorAll('.FilterModalCategoryChild').forEach(function (button) {
            if (button.textContent.trim() === categoryText) {
                button.classList.add('On');
            } else {
                button.classList.remove('On');
            }
        });
    }
});

modalCloseBtn.addEventListener("click", function () {
    filterModal.style.display = "none";
    document.body.style.overflow = "auto";
})

modalSaveBtn.addEventListener("click", function () {
    filterModal.style.display = "none";
    document.body.style.overflow = "auto";
    // CategoryList Category 버튼의 On 클래스 제거
    document.querySelectorAll('.CategoryList.Category.On').forEach(function (button) {
        button.classList.remove('On');
    });

    // FilterModalCategoryChild 중에서 On 클래스를 가진 버튼 찾아서 CategoryList Category에 On 클래스 추가
    const isFilterOn = document.querySelector('.FilterModalCategoryChild.On');

    if (isFilterOn) {
        document.querySelectorAll('.FilterModalCategoryChild.On').forEach(function (button) {
            const categoryText = button.textContent.trim();
            document.querySelectorAll('.CategoryList.Category').forEach(function (categoryButton) {
                if (categoryButton.textContent.trim() === categoryText) {
                    categoryButton.classList.add('On');
                    categoryInput.value = categoryButton.getAttribute('category');
                }
            });
        });
    } else {
        // On 클래스가 없는 경우 CategoryList Category 중에서 "전체"에 On 클래스 추가
        document.querySelectorAll('.CategoryList.Category').forEach(function (categoryButton) {
            if (categoryButton.textContent.trim() === '전체') {
                categoryButton.classList.add('On');
            } else {
                categoryButton.classList.remove('On');
            }
        });
    }

    sortType.value = modalSortType.value;
    reloadPage();
})

modalCategoryBtn.forEach(function (button) {
    button.addEventListener("click", function () {
        modalCategoryBtn.forEach(function (btn) {
            // 클릭한 버튼을 제외한 다른 버튼에서 "On" 클래스 제거
            if (btn !== button) {
                btn.classList.remove("On");
            }
        });

        // 클릭한 버튼에 "On" 클래스 토글
        button.classList.toggle("On");
    });
});

CategoryBtn.forEach(function (button) {
    button.addEventListener("click", function () {
        if (!button.classList.contains("On")) {
            CategoryBtn.forEach(function (btn) {
                btn.classList.remove("On");
            });
            button.classList.add("On");
            categoryInput.value = button.getAttribute("category");
            reloadPage();
        }
    })
});

modalResetBtn.addEventListener("click", function () {
    modalCategoryBtn.forEach(function (button) {
        button.classList.remove("On");
    });
});


/////////////
// 무한 스크롤

// 처음 로드시 ssr방식으로 첫번째 페이지는 불러와졌으므로 1부터 시작.
let page = 1;
let isLoading = false;
let isLastPage = false;

window.addEventListener('scroll', function () {
    if (isLoading === true || isLastPage === true) return;

    let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

    if (clientHeight + scrollTop >= scrollHeight) {
        getMoreContents();
    }
});

searchInput.addEventListener('keyup', (e) => {
    if (e.key === 'Enter') {
        reloadPage();
    }
});

const getMoreContents = async () => {
    const contentListContainer = contentsContainer.querySelector('.ContentListContainer');
    isLoading = true;

    await loadingMarkService.show(loadingMark);

    window.scrollTo({
        top: document.body.scrollHeight,
        behavior: 'smooth'
    });

    const {html, isLast} = await promotionListComponent(categoryInput.value, sortType.value, 0, searchInput.value);

    contentListContainer.insertAdjacentHTML("beforeend", html);
    isLastPage = isLast;

    await loadingMarkService.hide(loadingMark);

    page++;
    isLoading = false;
}

const reloadPage = async () => {
    isLoading = true;
    let htmlC;

    await loadingMarkService.show(loadingMark);

    const {html, isLast} = await promotionListComponent(categoryInput.value, sortType.value, 0, searchInput.value);

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
            <div class="ContentListContainer">
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