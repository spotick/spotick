const placeManageService = (function () {
    let dialogueString = "";

    function showGSForStopPlace(placeId) {
        dialogueString = "장소 대여를 중단하시겠습니까?<br>(대여 중지 시, 이미 들어온 대여 요청은 모두 취소 됩니다.)"
        showGlobalSelection(dialogueString, () => stopPlace(placeId));
    }

    // todo = 예약을 더이상 받지 않고 등록된 장소에서도 일시적으로 내려가지도록 장소 status 변경처리 요청 필요
    // placeId 변조가능성 있으므로 백엔드 측에서 검증 필수
    function stopPlace(placeId) {
        console.log(placeId);
    }

    function showGsForRemovePlace(placeId) {
        dialogueString = "장소를 삭제할 시<br>다시 되돌릴 수 없습니다!<br>등록한 장소를 삭제하시겠습니까?"
        showGlobalSelection(dialogueString, () => removePlace(placeId))
    }

    function removePlace(placeId) {
        console.log(placeId);
    }

    function showGsForResumePlace(placeId) {
        dialogueString = "장소 대여를 재개하시겠습니까?"
        showGlobalSelection(dialogueString, () => resumePlace(placeId))
    }

    function resumePlace(placeId) {
        console.log(placeId);
    }

    function editPlace(placeName, placeSubTitle, lat, lng, placeAddress, placeAddressDetail, placeInfo, placeRule, placeDefaultPrice, placeDefaultPeople, accountHolder, bankName, accountNumber) {
        openModal(modalPlace);

        resetMapAndMarkerByLatLng(lat, lng);

        const elementIds = ['placeName', 'placeSubTitle', 'placeAddress', 'placeAddressDetail', 'placeInfo', 'placeRule', 'placeDefaultPrice', 'placeDefaultPeople', 'accountHolder', 'accountNumber'];
        elementIds.forEach(function (elementId) {
            document.getElementById(elementId).value = eval(elementId);
        });

        const bankSelection = document.getElementById('bankName');
        switch (bankName) {
            case '국민':
                bankSelection.selectedIndex = 0;
                break;
            case '우리':
                bankSelection.selectedIndex = 1;
                break;
            case '농협':
                bankSelection.selectedIndex = 2;
                break;
        }
    }

    return {
        showGSForStopPlace: showGSForStopPlace,
        showGsForRemovePlace: showGsForRemovePlace,
        showGsForResumePlace: showGsForResumePlace,
        editPlace: editPlace
    }
})();