// 아이템 이미지 슬라이드
// 모든 OneItemContainer에 대해 반복
document.querySelectorAll('.OneItemContainer').forEach(function (container, index) {
    // 현재 container의 ImageSwiper 클래스를 가진 요소를 찾음
    var swiperContainer = container.querySelector('.ImageSwiper');

    // Swiper 초기화
    var swiper = new Swiper(swiperContainer, {
        pagination: {
            el: swiperContainer.querySelector('.swiper-pagination'),
            clickable: true,
        },
        navigation: {
            nextEl: swiperContainer.querySelector('.RightBtn'),
            prevEl: swiperContainer.querySelector('.LeftBtn'),
        },
        on: {
            slideChange: function () {
                handleSlideChange(this);

            },
        },
    });

    // 슬라이드 index최대값
    page(swiper);


    // 초기 페이지네이션 업데이트
    updatePagination(swiper);
});

function handleSlideChange(swiper) {
    var snapIndexElement = swiper.el.querySelector(".snapIndex");
    if (snapIndexElement) {
        snapIndexElement.textContent = swiper.snapIndex + 1;
    }
}

function page(swiper){
    var pageIndex = swiper.el.querySelector(".slideIndex");
    if (pageIndex) {
        pageIndex.textContent = swiper.slides.length;
    }
    // 슬라이드가 1개일 경우 hover 없애기
    if(swiper.slides.length === 1){
        // 부모 노드를 탐색하면서 hover 클래스를 가진 부모를 찾습니다.
        let parentWithHoverClass = null;
        let parentNode = pageIndex.parentElement;
        let parentContainer = pageIndex.parentElement.parentElement;
        console.log(parentContainer)

        while (parentNode) {
            if (parentNode.classList.contains("hover")) {
                parentWithHoverClass = parentNode;
                break;
            }
            parentNode = parentNode.parentElement;
        }

        if (parentWithHoverClass) {
            parentContainer.style.display = "none";
            parentWithHoverClass.classList.remove("hover");
        }
    }

}

function updatePagination(swiper) {
    swiper.pagination.update(); // Swiper에게 페이지네이션을 다시 그리도록 알림
}
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

const selectBoxBtn = document.querySelector('.SelectBoxBtn');
const selectBoxList = document.querySelector('.SelectBoxList');
const selectBoxBtnImg = document.querySelector('.SelectBoxBtnImg');
const SelectBoxBtnText = document.querySelector('.SelectBoxBtnText');

// 좋아요 버튼
function toggleLike(btn) {
    var likeIcon = btn.querySelector('.ItemLikeBtn img');

    // 이미지를 토글
    likeIcon.src = likeIcon.src.includes('heart_line') ? '../../static/imgs/heart_filled_white_shadow.708fbebd.png' : '../../static/imgs/heart_line_white_shadow.d5d214d0.png';
}

// 인기순 필터
selectBoxBtn.addEventListener('click', function () {
    // 토글 기능을 이용하여 리스트 보이기/숨기기
    selectBoxList.style.display = (selectBoxList.style.display === 'block') ? 'none' : 'block';

    // 이미지 변경
    selectBoxBtnImg.src = (selectBoxList.style.display === 'block') ? '../../static/imgs/arrow_up_gray014.75d8599e.svg' : '../../static/imgs/arrow_down_gray014.f502da9d.svg';
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
        selectBoxBtnImg.src = '../../static/imgs/arrow_down_gray014.f502da9d.svg';
    });
});

// 필터 모달창 나오기
document.querySelector('.FilterBtn').addEventListener("click", function(){
    document.querySelector('.FilterModalContainer').classList.add('On');
})

// 필터 모달창 숨기기
document.querySelector('.FilterModalCloseBtn').addEventListener("click", function(){
    document.querySelector('.FilterModalContainer').classList.remove('On');
})

// 필터 적용 버튼
document.querySelector('.FilterSubmitBtn').addEventListener("click", function(){
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
                    img.src = '../../static/imgs/rectangle_line_rainyBlue086.76bf0d5f.svg';
                });

                // 선택된 지역 리스트 초기화
                SelectItemContainer.innerHTML = '';

                changeSize(405);

                checkBoxImg.src = '../../static/imgs/checkRectangle_filled_sweetBlue046.0cd80fee.svg';
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

                    checkBoxImg.src = '../../static/imgs/rectangle_line_rainyBlue086.76bf0d5f.svg';

                    // 선택된 지역 리스트에서 아이템 삭제
                    const itemsToRemove = SelectItemContainer.querySelectorAll('.SelectedItemText');
                    itemsToRemove.forEach(item => {
                        if (item.textContent === checkBoxText.textContent) {
                            item.parentElement.parentElement.remove();
                        }
                    });
                });

                changeSize(405);

                checkBoxImg.src = '../../static/imgs/checkRectangle_filled_sweetBlue046.0cd80fee.svg';
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
            checkBoxImg.src = '../../static/imgs/rectangle_line_rainyBlue086.76bf0d5f.svg';

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
                if(!checkbox.checked && checkbox.name !== "전체"){
                    checkbox.disabled = true;
                }
            })
        }else{
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
function reset(){
    // 전체 체크박스 해제
    checkboxes.forEach(checkbox => {
        checkbox.checked = false;
    });

    checkboxImages.forEach(img => {
        img.src = '../../static/imgs/rectangle_line_rainyBlue086.76bf0d5f.svg';
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
