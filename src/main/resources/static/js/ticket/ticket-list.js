import {requestLike} from "../modules/likeFetch.js";
import {showTicketListEvent} from "../components/ticket/ticketComponent.js";
import {loadingMarkService} from "../modules/loadingMark.js";

// ============================================== 선언부
const categoryInput = document.getElementById('category');
const sortInput = document.getElementById('sort');
const rateInput = document.getElementById('rate')


// ===================================================================================================================
// 필터쪽 체크박스
const checkboxes = document.querySelectorAll('input[type="checkbox"]');
const allCheckboxes = document.querySelectorAll('input[name="전체"]');
const selectedListContainer = document.querySelector('.SelectedListContainer');
const SelectItemContainer = document.querySelector('.SelectItemContainer');

const areaGroupContainer = document.querySelector(".AreaGroupContainer");
const areasCityContainer = document.querySelectorAll(".AreasCityContainer");
const checkboxImages = document.querySelectorAll('.CheckBoxImg');
const resetButton = document.querySelector('.FilterResetBtn');

const areaGroupButtons = document.querySelectorAll('.AreaGroupBtn');

// 인기순 필터
const sortSelectBoxBtn = document.querySelector('#sortType .SelectBoxBtn');
const sortSelectBoxList = document.querySelector('#sortType .SelectBoxList');
const sortSelectBoxBtnImg = document.querySelector('#sortType .SelectBoxBtnImg');
const sortSelectBoxBtnText = document.querySelector('#sortType .SelectBoxBtnText');
const sortListItems = document.querySelectorAll('#sortType .SelectBoxListItem');

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
const ratingSelectBoxBtn = document.querySelector('#ratingType .SelectBoxBtn');
const ratingSelectBoxList = document.querySelector('#ratingType .SelectBoxList');
const ratingSelectBoxBtnImg = document.querySelector('#ratingType .SelectBoxBtnImg');
const ratingSelectBoxBtnText = document.querySelector('#ratingType .SelectBoxBtnText');
const ratingListItems = document.querySelectorAll('#ratingType .SelectBoxListItem');

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

// 필터 모달창 나오기
document.querySelector('.FilterBtn').addEventListener("click", function () {
    document.querySelector('.FilterModalContainer').classList.add('On');
})

// 필터 모달창 숨기기
document.querySelector('.FilterModalCloseBtn').addEventListener("click", function () {
    document.querySelector('.FilterModalContainer').classList.remove('On');
})

// 필터 적용 버튼
document.querySelector('.FilterSubmitBtn').addEventListener("click", function () {
    document.querySelector('.FilterModalContainer').classList.remove('On');
})

// 필터 구 단위 체크박스
checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', function () {
        const checkBoxContainer = this.parentElement.querySelector('.CheckBoxContainer');
        const checkBoxImg = checkBoxContainer.querySelector('.CheckBoxImg');
        const checkBoxText = checkBoxContainer.querySelector('.CheckBoxText');

        console.log(this.checked);

        if (this.checked) {
            if (checkBoxText.textContent.includes("전체")) {
                // 전체 체크박스 해제
                checkboxes.forEach(otherCheckbox => {
                    if (otherCheckbox !== checkbox) {
                        otherCheckbox.checked = false;
                    }
                });

                checkboxImages.forEach(img => {
                    img.src = '/imgs/rectangle_line_rainyBlue086.76bf0d5f.svg';
                });

                // 선택된 지역 리스트 초기화
                SelectItemContainer.innerHTML = '';

                changeSize(405);

                checkBoxImg.src = '/imgs/checkRectangle_filled_sweetBlue046.0cd80fee.svg';
                selectedListContainer.classList.add('On');

                // 선택된 지역 리스트에 아이템 추가
                const newItem = document.createElement('div');
                newItem.style.marginRight = '10px';
                newItem.innerHTML = `
          <div class="SelectedItem">
            <span class="SelectedItemText">${checkBoxText.textContent}</span>
          </div>
        `;
                SelectItemContainer.appendChild(newItem);
            } else {

                allCheckboxes.forEach(allCheckbox => {
                    allCheckbox.checked = false;

                    // 추가로 실행할 코드
                    const checkBoxContainer = allCheckbox.parentElement.querySelector('.CheckBoxContainer');
                    const checkBoxImg = checkBoxContainer.querySelector('.CheckBoxImg');
                    const checkBoxText = checkBoxContainer.querySelector('.CheckBoxText');

                    checkBoxImg.src = '/imgs/rectangle_line_rainyBlue086.76bf0d5f.svg';

                    // 선택된 지역 리스트에서 아이템 삭제
                    const itemsToRemove = SelectItemContainer.querySelectorAll('.SelectedItemText');
                    itemsToRemove.forEach(item => {
                        if (item.textContent === checkBoxText.textContent) {
                            item.parentElement.parentElement.remove();
                        }
                    });
                });

                changeSize(405);

                checkBoxImg.src = '' +
                    '/imgs/checkRectangle_filled_sweetBlue046.0cd80fee.svg';
                selectedListContainer.classList.add('On');

                // 선택된 지역 리스트에 아이템 추가
                const newItem = document.createElement('div');
                newItem.style.marginRight = '10px';
                newItem.innerHTML = `
          <div class="SelectedItem">
            <span class="SelectedItemText">${checkBoxText.textContent}</span>
          </div>
        `;
                SelectItemContainer.appendChild(newItem);
            }

        } else {
            checkBoxImg.src = '/imgs/rectangle_line_rainyBlue086.76bf0d5f.svg';

            // 선택된 지역 리스트에서 아이템 삭제
            const itemsToRemove = SelectItemContainer.querySelectorAll('.SelectedItemText');
            itemsToRemove.forEach(item => {
                if (item.textContent === checkBoxText.textContent) {
                    item.parentElement.parentElement.remove();
                }
            });
        }


        const checkedCheckboxes = document.querySelectorAll('input[type="checkbox"]:checked');
        const numberOfCheckedCheckboxes = checkedCheckboxes.length;

        if (numberOfCheckedCheckboxes === 0) {
            selectedListContainer.classList.remove('On');
            changeSize(465);
        }

        if (numberOfCheckedCheckboxes === 5) {
            checkboxes.forEach(checkbox => {
                if (!checkbox.checked && checkbox.name !== "전체") {
                    checkbox.disabled = true;
                }
            })
        } else {
            checkboxes.forEach(checkbox => {
                checkbox.disabled = false;
            })
        }
    });
});

// 필터 하단부분 생길때 위쪽 컨테이너 height 변경
function changeSize(size) {
    areaGroupContainer.style.height = size + "px";

    // NodeList를 배열로 변환하여 각 AreasCityContainer에 대해 루프를 돕니다.
    areasCityContainer.forEach(function (container) {
        container.style.height = size + "px";
    });
}

// 필터 리셋버튼
resetButton.addEventListener('click', function () {
    reset();
    changeSize(465);
});

// 체크박스 전체 해제
function reset() {
    // 전체 체크박스 해제
    checkboxes.forEach(checkbox => {
        checkbox.checked = false;
    });

    checkboxImages.forEach(img => {
        img.src = '/imgs/rectangle_line_rainyBlue086.76bf0d5f.svg';
    });

    // 선택된 지역 리스트 초기화
    SelectItemContainer.innerHTML = '';

    // On 클래스 제거
    selectedListContainer.classList.remove('On');
}

// 지역 변경 버튼
areaGroupButtons.forEach(button => {
    button.addEventListener('click', function () {
        // 모든 AreaGroupBtn에서 'On' 클래스 제거
        areaGroupButtons.forEach(btn => {
            btn.classList.remove('On');
        });
        checkboxes.forEach(checkbox => {
            checkbox.disabled = false;
        })

        // 클릭된 버튼에 'On' 클래스 추가
        button.classList.add('On');

        const targetId = button.dataset.target;
        toggleCityContainer(targetId);
        reset();
        changeSize(456);
    });
});

// 지역에 맞는 컨테이너 On
function toggleCityContainer(targetId) {
    // 모든 AreasCityContainer에 'On' 클래스를 제거
    document.querySelectorAll('.AreasCityContainer').forEach(container => {
        container.classList.remove('On');
    });

    // 클릭된 버튼에 대응하는 AreasCityContainer에 'On' 클래스를 추가
    const targetContainer = document.getElementById(targetId);
    if (targetContainer) {
        targetContainer.classList.add('On');
    }
}

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
            return showTicketListEvent(page, categoryInput.value, rateInput.value, sortInput.value, postContainer);
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
            return showTicketListEvent(page, categoryInput.value, rateInput.value, sortInput.value, postContainer);
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
