const filterModal = document.getElementById('filterModal');
const checkboxes = document.querySelectorAll('.checkBox');
const allCheckboxes = document.querySelectorAll('.checkBox.all');
const areaButtons = document.querySelectorAll('.AreaGroupBtn');
const areaGroupContainer = document.getElementById('areaGroupContainer')
const areasCityContainer = document.querySelectorAll(".AreasCityContainer");
const selectedListContainer = document.getElementById('selectedListContainer')
const selectItemContainer = document.querySelector('.selectItemContainer');
// 선택된 컨테이너의 갯수를 파악하여 컨트롤하기위해 프로퍼티 선언.
selectItemContainer.checkboxCounter = 0;

const districtView = document.getElementById('districtView');

/*
* district null일 시, 조회조건 없음
* district 값이 있고 detailDistrict 비어있을 시 도시 기준으로 조회
* district 값이 있고 detailDistrict 있을 시 도시 + 상세로 조회
* */
export let districtFilter = {
    district: null,
    detailDistrict: []
};

////////////////////////////////////////////////////////////// 함수 선언
const resetCheckboxes = () => {
    checkboxes.forEach(checkbox => {
        checkbox.checked = false;
    });

    selectItemContainer.checkboxCounter = 0;
    selectItemContainer.innerHTML = ``;
}


const checkboxEvent = (checkbox) => {
    districtFilter.district = document.querySelector('.AreaGroupBtn.On').getAttribute('data-target');
    const detailDistrict = checkbox.name;
    // 체크 되었을 시
    if (checkbox.checked === true) {
        // ~ 전체
        if (checkbox.classList.contains("all")) {
            resetCheckboxes();
            checkbox.checked = !checkbox.checked;

            selectItemContainer.checkboxCounter = 1;
            districtFilter.detailDistrict = [];
        }
        // 상세 지역구 버튼일 시
        else {
            allCheckboxes.forEach(cb => {
                cb.checked = false;
                const selectedItem = document.querySelector(`.SelectedItem[data-target="${cb.name}"]`);
                if (selectedItem) {
                    selectedItem.remove();
                }
            });

            selectItemContainer.checkboxCounter++;
            districtFilter.detailDistrict.push(detailDistrict);
        }

        // 공용 내용
        const html = `
            <div class="SelectedItem" data-target="${detailDistrict}">
                <span class="SelectedItemText">${detailDistrict}</span>
                <button type="button" class="SelectedItemDeleteBtn">
                    <img src="/imgs/cross_1line_gray054.5b1e8cb9.svg" alt="삭제" class="SelectedItemDeleteBtnImg">
                </button>
            </div>
        `;

        selectItemContainer.insertAdjacentHTML("beforeend", html);

        if (selectItemContainer.checkboxCounter !== 0) {
            changeSize(405);
            selectedListContainer.classList.add("On");
        }

    }

    // 체크 해제 되었을 시
    else {
        selectItemContainer.checkboxCounter--;

        const selectedItem = document.querySelector(`.SelectedItem[data-target="${detailDistrict}"]`);
        if (selectedItem) {
            selectedItem.remove();
        }

        if (selectItemContainer.checkboxCounter === 0) {
            districtFilter.district = null;
            selectedListContainer.classList.remove("On");
            changeSize(465);
        }

        const index = districtFilter.detailDistrict.indexOf(detailDistrict);
        districtFilter.detailDistrict.splice(index, 1);
    }
}

function changeSize(size) {
    areaGroupContainer.style.height = `${size}px`;

    areasCityContainer.forEach(function (container) {
        container.style.height = `${size}px`;
    });
}

function setArea() {
    if (districtView) {
        districtFilter.district === null
            ? districtView.innerHTML = '지역 전체'
            : districtView.innerHTML = districtFilter.district + " " + districtFilter.detailDistrict.join(' ')
    }
}

///////////////////////////////////////////////////////////////// 이벤트리스너 선언
document.getElementById('filterBtn').addEventListener('click', () => {
    filterModal.classList.add("On");
});

document.getElementById('filterModalClose').addEventListener('click', () => {
    filterModal.classList.remove("On");
});

document.getElementById('filterResetBtn').addEventListener('click', () => {
    selectItemContainer.checkboxCounter = 0;
    selectedListContainer.classList.remove("On");
    changeSize(465);
    resetCheckboxes();
    districtFilter.district = null;
    districtFilter.detailDistrict = [];
    if (districtView) {
        districtView.innerHTML = "지역 전체";
    }
});

areaButtons.forEach(button => {
    button.addEventListener('click', () => {
        const target = button.getAttribute('data-target');

        districtFilter.district = null;
        districtFilter.detailDistrict = [];

        button.classList.add('On');

        areaButtons.forEach(otherButton => {
            if (otherButton !== button) {
                otherButton.classList.remove('On');
            }
        });

        areasCityContainer.forEach(container => {
            if (container.id === target) {
                container.classList.add('On');
            } else {
                container.classList.remove('On');
            }
        });
        selectItemContainer.checkboxCounter = 0;
        selectedListContainer.classList.remove("On");
        changeSize(465);
        resetCheckboxes();
    });
});

checkboxes.forEach(checkbox => {
    checkbox.addEventListener('change', () => checkboxEvent(checkbox));
});

selectItemContainer.addEventListener('click', (e) => {
    const selectedItem = e.target.closest('.SelectedItem');
    if (selectedItem) {
        selectItemContainer.checkboxCounter--;
        const detailDistrict = selectedItem.getAttribute('data-target');
        checkboxes.forEach(checkbox => {
            if (checkbox.name === detailDistrict) {
                checkbox.checked = false;
            }
        });

        selectedItem.remove();

        if (selectItemContainer.checkboxCounter === 0) {
            districtFilter.district = null;
            selectedListContainer.classList.remove("On");
            changeSize(465);
        }

        const index = districtFilter.detailDistrict.indexOf(detailDistrict);
        districtFilter.detailDistrict.splice(index, 1);
    }
});

export const setAreaAndCallback = (callback) => {
    setArea();
    filterModal.classList.remove("On");

    callback();
}