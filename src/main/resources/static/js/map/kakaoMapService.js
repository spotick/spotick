import { data } from "./map.js";

var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
  center : new kakao.maps.LatLng(37.50027256229777, 127.03649415283012), // 지도의 중심좌표
  level : 12 // 지도의 확대 레벨
});






// 마커 클러스터러를 생성합니다
// 마커 클러스터러를 생성할 때 disableClickZoom 값을 true로 지정하지 않은 경우
// 클러스터 마커를 클릭했을 때 클러스터 객체가 포함하는 마커들이 모두 잘 보이도록 지도의 레벨과 영역을 변경합니다
// 이 예제에서는 disableClickZoom 값을 true로 설정하여 기본 클릭 동작을 막고
// 클러스터 마커를 클릭했을 때 클릭된 클러스터 마커의 위치를 기준으로 지도를 1레벨씩 확대합니다
let clusterer = new kakao.maps.MarkerClusterer({
  map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
  averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
  minLevel: 8, // 클러스터 할 최소 지도 레벨
  disableClickZoom: true, // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
  // styles:[{
  //   display:'flex',
  // }]
});

// 데이터를 가져오기 위해 jQuery를 사용합니다
// 데이터를 가져와 마커를 생성하고 클러스터러 객체에 넘겨줍니다
let markers = $.map(data.positions, function(position) {
  return new kakao.maps.Marker({
      position: new kakao.maps.LatLng(position.lat, position.lng)
  });
});

clusterer.addMarkers(markers);

kakao.maps.event.addListener( clusterer, 'clusterclick', function( cluster ) {
  console.log( cluster.getSize() );
});

// 마커 클러스터러에 클릭이벤트를 등록합니다
// 마커 클러스터러를 생성할 때 disableClickZoom을 true로 설정하지 않은 경우
// 이벤트 헨들러로 cluster 객체가 넘어오지 않을 수도 있습니다
kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {

  // 현재 지도 레벨에서 1레벨 확대한 레벨
  var level = map.getLevel()-1;

  // 지도를 클릭된 클러스터의 마커의 위치를 기준으로 확대합니다
  map.setLevel(level, {anchor: cluster.getCenter()});
});