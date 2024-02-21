import {addSlideEvent} from '../../global-js/image-slide.js'

// 무한 페이징
let page = 1;
let hasNext = true;
let pagingTargetIdx = 1;
let sort = 'POPULARITY';

let area = {
    city: null,
    address: []
};

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

const selectBoxBtn = document.querySelector('.SelectBoxBtn');
const selectBoxList = document.querySelector('.SelectBoxList');
const selectBoxBtnImg = document.querySelector('.SelectBoxBtnImg');
const SelectBoxBtnText = document.querySelector('.SelectBoxBtnText');


// 인기순 필터

selectBoxBtn.addEventListener('click', function () {
    // 토글 기능을 이용하여 리스트 보이기/숨기기
    selectBoxList.style.display = (selectBoxList.style.display === 'block') ? 'none' : 'block';
    // todo 선택된 정렬기준에 따라서 리스트 게시글 정렬하기
    // 이미지 변경
    selectBoxBtnImg.src = (selectBoxList.style.display === 'block') ? '/imgs/arrow_up_gray014.75d8599e.svg' : '/imgs/arrow_down_gray014.f502da9d.svg';
});


// 각 리스트 아이템에 대한 이벤트 리스너 추가
const listItems = document.querySelectorAll('.SelectBoxListItem');
listItems.forEach(item => {
    item.addEventListener('click', function () {
        // 선택된 아이템에 SelectBoxListItem-select 클래스 추가
        listItems.forEach(otherItem => {
            otherItem.classList.remove('SelectBoxListItem-select');
        });
        this.classList.add('SelectBoxListItem-select');

        // 선택된 아이템의 텍스트로 버튼 텍스트 변경
        SelectBoxBtnText.textContent = this.textContent;
        // 리스트 숨기기
        selectBoxList.style.display = 'none';

        // 이미지 변경
        selectBoxBtnImg.src = '/imgs/arrow_down_gray014.f502da9d.svg';
        sort = this.dataset.sort;
        resetListPagination();
        getPlaceList();

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
    let city = $('.AreaGroupBtn.On').text();
    let $input = $('input:checkbox:checked');

    area.address.length = 0;
    if ($input.length === 0) {
        area.city = null;
    } else {
        area.city = city;
        $input.each((i, item) => {
            if (item.name !== '전체') {
                area.address.push(item.name);
            }
        });
    }
    resetListPagination();
    getPlaceList();
    document.querySelector('.FilterModalContainer').classList.remove('On');
})

// 필터 구 단위 체크박스
checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', function () {
        const checkBoxContainer = this.parentElement.querySelector('.CheckBoxContainer');
        const checkBoxImg = checkBoxContainer.querySelector('.CheckBoxImg');
        const checkBoxText = checkBoxContainer.querySelector('.CheckBoxText');

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
            <button type="button" class="SelectedItemDeleteBtn">
              <img src="/imgs/cross_1line_gray054.5b1e8cb9.svg" alt="삭제" class="SelectedItemDeleteBtnImg">
            </button>
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

                checkBoxImg.src = '/imgs/checkRectangle_filled_sweetBlue046.0cd80fee.svg';
                selectedListContainer.classList.add('On');

                // 선택된 지역 리스트에 아이템 추가
                const newItem = document.createElement('div');
                newItem.style.marginRight = '10px';
                newItem.innerHTML = `
          <div class="SelectedItem">
            <span class="SelectedItemText">${checkBoxText.textContent}</span>
            <button type="button" class="SelectedItemDeleteBtn">
              <img src="/imgs/cross_1line_gray054.5b1e8cb9.svg" alt="삭제" class="SelectedItemDeleteBtnImg">
            </button>
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
    area.city = null;
    area.address.length = 0;
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


// 좋아요 버튼
$(`.ListItemsContainer`).on('click', '.ItemBookMarkBtn', function () {
    let isLoggedIn = $('#isLoggedIn').val();
    if (isLoggedIn === 'false') {
        alert('로그인이 필요한 서비스 입니다');
        location.href = '/user/login';
        return;
    }
    let placeId = $(this).data('placeid');
    let target = $(this).closest('.OneItemContainer').find('.bookmark-count');
    let bookmarkCnt = Number(target.text());
    fetch(`/bookmark?placeId=${placeId}`)
        .then(response => response.json())
        .then(isAdded =>
            target.text(isAdded ? ++bookmarkCnt : --bookmarkCnt)
        );
    $(this).find('span').toggleClass('none');
});

function getPlaceList() {
    fetch(`/place/api/list?page=${page++}&sort=${sort}${area.city==null?'':'&area='+encodeURIComponent(JSON.stringify(area))}`)
        .then(response => {
            if (!response.ok) {
                throw new Error();
            }
            return response.json();
        }).then(data => {
        hasNext = !data.last;
        displayPlaceList(data)
    });
}


function displayPlaceList(data) {
    let text = '';

    data.content.forEach(place => {
        text += `
       <div class="OneItemContainer hover">
                <div class="OneItemImgContainer">
                    <div class="swiper ImageSwiper swiper-initialized swiper-horizontal swiper-pointer-events swiper-backface-hidden">
                        <a href="/place/detail/${place.id}"
                           class="swiper-wrapper ImageLength" style="transform: translate3d(0px, 0px, 0px);" >`;
        place.placeFiles.forEach(placeImg => {
            text += `
                           <div class="swiper-slide swiper-slide-active" style="width: 287px;">
                               <img class="ItemImg"
                                    height="1350.6666666666665px" 
                                    alt="${place.title}" src="/file/display?fileName=${placeImg.uploadPath}/t_${placeImg.uuid}_${placeImg.fileName}">
                           </div>`;
        });
        text += `     </a>
                       <div class="NavigationBtnContainer">
                           <button class="NavigationBtn RightBtn" type="button">
                               <img alt="다음" src="/imgs/round_arrow_right_gray024.7f7e18a3.svg">
                           </button>
                           <button class="NavigationBtn LeftBtn" type="button">
                               <img alt="이전" src="/imgs/round_arrow_left_gray024.707193e8.svg">
                           </button>
                       </div>
                       <div class="ItemImgPagination">
                           <p><span class="snapIndex">1</span>/5</p>
                       </div>
                   </div>
                   <button class="ItemBookMarkBtn" data-placeid="${place.id}" type="button">
                       <span class="${!place.bookmarkChecked ? '' : 'none'}"><i
                               class="fa-regular fa-bookmark"></i></span>
                       <span class="${place.bookmarkChecked ? '' : 'none'}"><i class="fa-solid fa-bookmark"
                                                                                style="color: white"></i></span>
                   </button>
               </div>
               <div class="ItemTextContainer">
                   <div class="ItemHostNameContainer">
                       <span class="ItemHostName">${place.placeAddress.address}</span>
                       <div class="ItemCountsContainer">
                           <div class="ItemsStarCountContainer">
                               <img alt="후기갯수" class="ItemCountImg"
                                    src="/imgs/star_filled_paintYellow056.a8eb6e44.svg">
                               <span class="ItemCountText">${place.evalAvg}(${place.evalCount})</span>
                           </div>
                           <div class="ItemsLikeCountContainer">
                               <img alt="북마크갯수" class="bookmark-img" src="/imgs/bookmark_thin.svg">
                               <span class="ItemCountText bookmark-count">${place.bookmarkCount}</span>
                           </div>
                       </div>
                   </div>
                   <div class="ItemSpaceNameContainer">
                       <p class="ItemSpaceName" >
                           <a href="/place/detail/${place.id}">${place.title}</a>
                       </p>
                   </div>
                   <div class="ItemPriceContainer">
                       <span class="place-price">${(place.price).toLocaleString()}원</span>
                   </div>
               </div>
           </div>
    `;
    })

    $('.ListItemsContainer').append(text);
    // 동적으로 추가된 게시글요소에 이미지 슬라이드 이벤트 추가하기
    addSlideEvent();
}


window.addEventListener('scroll', function () {
    if (!hasNext) return;

    // 가장 첫 번째 요속가 화면에서 사랴지면 페이징 불러오기
    // -> 자연스러운 무한페이징
    let itemContainers = document.querySelectorAll('.OneItemContainer');
    let {bottom} = itemContainers[pagingTargetIdx - 1].getBoundingClientRect();
    if (bottom < 0) {
        pagingTargetIdx += 12;
        getPlaceList();
    }
});

function resetListPagination() {
    page = 0;
    pagingTargetIdx = 1;
    $('.ListItemsContainer').html('');
}





















