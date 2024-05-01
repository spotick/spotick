import {placeService} from "../../services/place/placeService.js";
import {placeLayouts} from "../../layouts/place/placeLayouts.js";

/*
* 장소리스트를 요청하는 컴포넌트와 layout을 혼합한 비동기 컴포넌트<br>
* html과 isLast 반환
* @Param(page) NotNull 가져올 페이지
* @Param(sort) Nullable 정렬 방법 지정; 가져올 리스트의 정렬 방법을 지정한다; 디폴트 값: POPULARITY
* @Param(districtFilter) Nullable 지역 분류 필터; 선택된 지역을 필터링 한다.
* @Param(keyword) Nullable 키워드 필터; 키워드값에 들어간 단어만 찾아 필터링 한다.
* */
export const slicePlaceListComponents = async (page, sort, district, detailDistrict, keyword) => {
    return placeService.getList(page, sort, district, detailDistrict, keyword, placeLayouts.placeListLayout);
}