import {placeService} from "../services/place/placeService.js";

let page = 0;

window.onload = () => {
    placeService.getList(page, null, null, "서울");
}