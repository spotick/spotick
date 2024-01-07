// 지도를 표시할 div와 초기 환경 설정
const mapContainer = document.getElementById('map');
const mapOption = {
    center: new kakao.maps.LatLng(37.49947087294, 127.0358208842),
    level: 3
};

// 지도 생성
const map = new kakao.maps.Map(mapContainer, mapOption);

// 주소-좌표 변환 객체와 장소 검색 객체 생성
const geocoder = new kakao.maps.services.Geocoder();
const ps = new kakao.maps.services.Places();

// 마커와 인포윈도우 생성
const marker = new kakao.maps.Marker();
const infowindow = new kakao.maps.InfoWindow({zindex: 1});

// 현재 지도 중심좌표로 주소를 검색해서 지도 좌측 상단에 표시
searchAddrFromCoords(map.getCenter());

// 지도를 클릭했을 때의 이벤트 리스너
kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
    // 클릭한 위치의 좌표로 주소를 검색
    searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
        let address = result[0].address.address_name;
        let addressTokens = address.split(" ");

        // 검색이 성공적으로 수행되지 않으면 이 함수를 빠져나감
        if (status !== kakao.maps.services.Status.OK) return;

        // 마커설정 및 인포윈도우에 주소 표시
        let detailAddr = result[0].road_address ? `${result[0].road_address.address_name}` : '';

        marker.setPosition(mouseEvent.latLng);
        marker.setMap(map);

        let lat = mouseEvent.latLng.getLat();
        let lng = mouseEvent.latLng.getLng();

        $('#placeLat').val(lat);
        $('#placeLng').val(lng);

        $('#placeAddress').val(detailAddr);
    });
});

// 지도가 이동하면 새로운 중심 좌표에 대한 주소를 검색
kakao.maps.event.addListener(map, 'idle', function () {
    searchAddrFromCoords(map.getCenter());
});

// 주어진 좌표로부터 주소를 검색하는 함수
function searchAddrFromCoords(coords) {
    geocoder.coord2RegionCode(coords.getLng(), coords.getLat());
}

// 주어진 좌표로부터 상세 주소를 검색하는 함수
function searchDetailAddrFromCoords(coords, callback) {
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
}

// 장소 검색 결과를 처리하는 콜백 함수
function placesSearchCB(data, status, pagination) {
    // 검색이 성공적으로 수행되지 않으면 이 함수를 빠져나감
    if (status !== kakao.maps.services.Status.OK) return;

    const place = data[0];
    const latLng = new kakao.maps.LatLng(place.y, place.x);
    marker.setPosition(latLng);
    marker.setMap(map);

    console.log(latLng.getLat());
    console.log(latLng.getLng());

    $('#placeLat').val(latLng.getLat());
    $('#placeLng').val(latLng.getLng());

    searchDetailAddrFromCoords(latLng, function (result, status) {
        if (status !== kakao.maps.services.Status.OK) return;  // 조기 반환

        let detailAddr = !!result[0].road_address ? `${result[0].road_address.address_name}` : '';

        $('#placeAddress').val(detailAddr);

    });
    map.setCenter(latLng);
}


// 검색버튼 지도 검색
$('.search-btn').on('click', mapSearch);

// input칸 엔터 이벤트
$('#search').on('keypress', function (e) {
    if (e.code == 'Enter' && $('#search').val().length > 0) {
        mapSearch();
    }
});

// 검색 실행
function mapSearch() {
    let keyword = $('#search').val();
    ps.keywordSearch(keyword, placesSearchCB);
}

// 위도, 경도에 따른 맵 마커 생성 및 화면 이동 함수화
function resetMapAndMarkerByLatLng(lat, lng) {
    // 맵 재생성.(생성시 유의사항 : 맵이 보여지는 상태에서 재생성해줘야 보여지므로 서순에 유의할 것)
    map.relayout();

    // 화면 이동
    var moveLatLon = new kakao.maps.LatLng(lat, lng);

    // 지도 중심을 이동 시킵니다
    map.setCenter(moveLatLon);

    // 마커 재배치
    var markerPosition  = new kakao.maps.LatLng(lat, lng);

    var marker = new kakao.maps.Marker({
        position: markerPosition
    });

    var iwContent = '<div style="padding:5px; margin-left: 18px"><span>기존 지정 위치</span></div>',
        iwPosition = new kakao.maps.LatLng(lat, lng);

    var infowindow = new kakao.maps.InfoWindow({
        position : iwPosition,
        content : iwContent
    });
    infowindow.open(map, marker);

    marker.setMap(map);
}














