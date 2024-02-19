const placeTitle = document.getElementById('placeTitle');
const placeSubTitle = document.getElementById('placeSubTitle');
const placeAddress = document.getElementById('placeAddress');
const placeAddressDetail = document.getElementById('placeAddressDetail');
const savedImgWrap = document.getElementById('savedImgs');
const savedImgPagination = document.getElementById('savedImgPagination');
const imageFileWrap = document.getElementById('imageFileWrap');
const placeInfo = document.getElementById('placeInfo');
const placeRule = document.getElementById('placeRule');
const placePrice = document.getElementById('placePrice');
const placeDefaultPeople = document.getElementById('placeDefaultPeople');
const accountHolder = document.getElementById('accountHolder');
const accountNumber = document.getElementById('accountNumber');
const bankName = document.getElementById('bankName');
const deleteList = document.getElementById('deleteList');
const requestFile = document.getElementById('requestFile');


const placeManageService = (function () {
    let dialogueString = "";

    function disablePlace(placeId) {
        dialogueString = "장소 대여를 중단하시겠습니까?<br>(대여 중지 시, 이미 들어온 대여 요청은 모두 취소 됩니다.)"
        showGlobalSelection(dialogueString, () => requestDisable(placeId));
    }

    function requestDisable(placeId) {
        fetch(`/place/api/disable/` + placeId, {
            method: 'PATCH'
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw response
                }
            })
            .then(message => {
                alert(message);
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                closeOnlyThisModal(globalSelection);

                error.text().then(message => showGlobalDialogue(message))
            });
    }

    function enablePlace(placeId) {
        dialogueString = "장소를 다시 활성화 시키겠습니까?";
        showGlobalSelection(dialogueString, () => requestEnable(placeId))
    }

    function requestEnable(placeId) {
        fetch(`/place/api/approved/${placeId}`, {
            method: 'PATCH'
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw response
                }
            })
            .then(message => {
                alert(message);
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                closeOnlyThisModal(globalSelection);

                error.text().then(message => showGlobalDialogue(message))
            });
    }

    function deletePlace(placeId) {
        dialogueString = "장소를 삭제할 시<br>다시 되돌릴 수 없습니다!<br>등록한 장소를 삭제하시겠습니까?"
        showGlobalSelection(dialogueString, () => requestDelete(placeId))
    }

    function requestDelete(placeId) {
        fetch(`/place/api/delete/${placeId}`, {
            method: 'PATCH'
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw response
                }
            })
            .then(message => {
                alert(message);
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                closeOnlyThisModal(globalSelection);

                error.text().then(message => showGlobalDialogue(message))
            });
    }

    function requestPlaceInfo(placeId) {
        fetch(`/place/api/get/${placeId}`, {
            method: 'GET'
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
            })
            .then(data => {
                console.log(data);

                setUpEditModal(data);
            })
            .catch(error => {
                console.error('Error:', error);
                closeOnlyThisModal(globalSelection);

                error.text().then(message => showGlobalDialogue(message))
            });
    }

    function setUpEditModal(data) {
        openModal(modalPlace);

        resetMapAndMarkerByLatLng(data.lat, data.lng);

        placeTitle.value = data.title;
        placeSubTitle.value = data.subtitle;
        placeAddress.value = data.postAddress.address;
        placeAddressDetail.value = data.postAddress.addressDetail;

        deleteList.innerHTML = '';
        savedImgWrap.currentPage = 0;
        savedImgWrap.style.transform = `translateX(0px)`;
        imgSlideService.setImgSlides(data.placeFiles, savedImgWrap);
        imgSlideService.setSlidePagination(data.placeFiles.length, savedImgPagination);
    }

    function requestUpdatePlace(placeId) {
        const deletedImgList = Array
            .from(deleteList.querySelectorAll('input'))
            .map(input => Number(input.value));

        const files = requestFile.value;

        const requestBody = {
            deletedImgList: deletedImgList,
            files: files
        };

        fetch(`/place/api/update/${placeId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody),
        })
            .then(response => {
                if (response.ok) {
                    console.log('정상 실행');
                } else {
                    throw new Error();
                }
            })
            .catch(error => {
                console.log(error)
            });
    }

    return {
        disablePlace: disablePlace,
        enablePlace: enablePlace,
        deletePlace: deletePlace,
        requestPlaceInfo: requestPlaceInfo,
        requestUpdatePlace: requestUpdatePlace
    }
})();

function deleteImg(container, dataId) {
    container.querySelectorAll('.imgSlide-item').forEach(div => {
        if (div.getAttribute('data-id') === dataId) {
            const isLastChild = div === container.lastElementChild;
            console.log(isLastChild);

            container.slideLength--;
            div.remove();

            const input = document.createElement('input');
            input.type = 'hidden';
            input.value = dataId;
            deleteList.appendChild(input);

            savedImgPagination.lastElementChild.remove();

            if (isLastChild) {
                imgSlideService.moveSlide("left", savedImgWrap, () => changeActive(savedImgWrap.currentPage))
            }
        }
    })
}

/////////////////////////////////////////////////////////////

document.querySelectorAll('.disablePlace').forEach(place => {
    place.addEventListener('click', function () {
        let placeId = this.parentElement.getAttribute('data-id');

        placeManageService.disablePlace(placeId);
    })
})

document.querySelectorAll('.enablePlace').forEach(place => {
    place.addEventListener('click', function () {
        let placeId = this.parentElement.getAttribute('data-id');

        placeManageService.enablePlace(placeId);
    })
})

document.querySelectorAll('.deletePlace').forEach(place => {
    place.addEventListener('click', function () {
        let placeId = this.parentElement.getAttribute('data-id');

        placeManageService.deletePlace(placeId);
    })
})

document.querySelectorAll('.editPlace').forEach(place => {
    place.addEventListener('click', function () {
        let placeId = this.parentElement.getAttribute('data-id');

        window.location.href = `/place/edit/${placeId}`;
    })
})

document.getElementById('savedLeft').addEventListener('click', () => {
    imgSlideService.moveSlide('left', savedImgWrap, () => changeActive(savedImgWrap.currentPage));
})

document.getElementById('savedRight').addEventListener('click', () => {
    imgSlideService.moveSlide('right', savedImgWrap, () => changeActive(savedImgWrap.currentPage));
})

function changeActive(index) {
    const buttons = savedImgPagination.querySelectorAll('.imgSlide-pagination-btn');

    buttons.forEach(button => button.classList.remove('active'));
    buttons[index].classList.add('active')
}

savedImgPagination.addEventListener('click', (e) => {
    if (e.target.classList.contains('imgSlide-pagination-btn')) {
        const index = e.target.getAttribute('index');

        imgSlideService.getSlidePage(index, savedImgWrap);

        const buttons = savedImgPagination.querySelectorAll('.imgSlide-pagination-btn');

        buttons.forEach(button => button.classList.remove('active'));
        e.target.classList.add('active');
    }
})

