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
    let likeIcon = btn.querySelector('.ItemLikeBtn img');

    // 이미지를 토글
    likeIcon.src = likeIcon.src.includes('heart_line') ? '/imgs/heart_filled_white_shadow.708fbebd.png' : '/imgs/heart_line_white_shadow.d5d214d0.png';
}

// 인기순 필터
selectBoxBtn.addEventListener('click', function () {
    // 토글 기능을 이용하여 리스트 보이기/숨기기
    selectBoxList.style.display = (selectBoxList.style.display === 'block') ? 'none' : 'block';

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
