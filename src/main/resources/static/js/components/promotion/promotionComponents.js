import {promotionService} from "../../services/promotion/promotionService.js";
import {promotionLayouts} from "../../layouts/promotion/promotionLayouts.js";

export const promotionListOfUserComponent = async (userId, promotionId, page) => {
    return await promotionService.getListOfUser(userId, promotionId, page, promotionLayouts.promotionMoreContentsLayout);
}