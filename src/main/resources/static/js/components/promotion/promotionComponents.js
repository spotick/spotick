import {promotionService} from "../../services/promotion/promotionService.js";
import {promotionLayouts} from "../../layouts/promotion/promotionLayouts.js";

export const promotionListComponent = async (category, sortType, page) => {
    return await promotionService.getList(category, sortType, page, promotionLayouts.promotionListLayout);
}

export const promotionListOfUserComponent = async (userId, promotionId, page) => {
    return await promotionService.getListOfUser(userId, promotionId, page, promotionLayouts.promotionMoreContentsLayout);
}