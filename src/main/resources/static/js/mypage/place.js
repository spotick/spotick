import {showGlobalDialogue, showGlobalSelection} from "../global-js/global-modal.js";
import {placeService} from "../services/place/placeService.js";

////
// 장소 비활성화
document.querySelectorAll('.disablePlace').forEach(place => {
    place.addEventListener('click', function () {
        const placeId = this.parentElement.getAttribute('data-id');

        showGlobalSelection(
            "장소 대여를 중단하시겠습니까?<br>(대여 중지 시, 이미 들어온 대여 요청은 모두 취소 됩니다.)",
            () => disablePlace(placeId)
        );
    });
});

const disablePlace = async (placeId) => {
    const {success, message} = await placeService.disablePlaceService(placeId);

    if (success) {
        alert(message);
        window.location.reload();
    } else {
        showGlobalDialogue(message);
    }
}

// 장소 활성화
document.querySelectorAll('.enablePlace').forEach(place => {
    place.addEventListener('click', function () {
        const placeId = this.parentElement.getAttribute('data-id');

        showGlobalSelection(
            "장소를 다시<br>활성화 시키겠습니까?",
            () => reopenPlace(placeId)
        );
    });
});

const reopenPlace = async (placeId) => {
    const {success, message} = await placeService.reopenPlaceService(placeId);

    if (success) {
        alert(message);
        window.location.reload();
    } else {
        showGlobalDialogue(message);
    }
}

// 장소 삭제
document.querySelectorAll('.deletePlace').forEach(place => {
    place.addEventListener('click', function () {
        const placeId = this.parentElement.getAttribute('data-id');

        showGlobalSelection(
            "장소를 삭제할 시<br>다시 되돌릴 수 없습니다!<br>등록한 장소를 삭제하시겠습니까?",
            () => deletePlace(placeId)
        );
    });
});

const deletePlace = async (placeId) => {
    const {success, message} = await placeService.deletePlace(placeId);

    if (success) {
        alert(message);
        window.location.reload();
    } else {
        showGlobalDialogue(message);
    }
}

// 장소 수정
document.querySelectorAll('.editPlace').forEach(place => {
    place.addEventListener('click', function () {
        const placeId = this.parentElement.getAttribute('data-id');

        window.location.href = `/place/edit/${placeId}`;
    });
});

// sort
const selectButton = document.querySelector('.select-box-btn');
const selectButtonImg = selectButton.querySelector('img');
const selectBoxList = document.querySelector('.select-box-list');

const selectBoxItems = document.querySelectorAll('.select-box-list button');

selectButton.addEventListener('click', () => {
    selectBoxList.classList.toggle('show');
    transformSelectBoxImg(selectBoxList.classList.contains('show'));
});

selectBoxItems.forEach(item => {
    item.addEventListener('click', () => {
        const sortType = item.getAttribute('sortType');

        window.location.href = `/mypage/places?page=${currentPage}&sort=${sortType}`;
    });
});

const transformSelectBoxImg = (boo) => {
    boo ? selectButtonImg.style.transform = 'rotate(180deg)' : selectButtonImg.style.transform = 'rotate(0deg)';
}
