var map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.3595704, 127.105399),
    zoom: 15
});

var marker = new naver.maps.Marker({
    position: new naver.maps.LatLng(37.3595704, 127.105399),
    map: map
});

naver.maps.Event.addListener(map, 'click', function(e) {
    marker.setPosition(e.latlng);
});

// var map = new naver.maps.Map('map', mapOptions);