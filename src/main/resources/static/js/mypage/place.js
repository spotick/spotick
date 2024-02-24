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

    return {
        disablePlace: disablePlace,
        enablePlace: enablePlace,
        deletePlace: deletePlace
    }
})();

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

