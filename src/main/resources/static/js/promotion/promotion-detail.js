import {promotionListOfUserComponent} from "../components/promotion/promotionComponents.js";
import {promotionLayouts} from "../layouts/promotion/promotionLayouts.js";
import {loadingMarkService} from "../modules/loadingMark.js";

const shareBtn = document.querySelector(".SideMenuLink")
const modalCloseBtn = document.querySelector(".ShareModalCloseBtn")
const modalBackground = document.querySelector(".ModalBackground")

const promotionId = document.getElementById('promo').value;
const userId = document.getElementById('user').value;

const moreContentsBlock = document.getElementById('moreContentsBlock');
const moreContentsContainer = document.querySelector('.CategoryContentsContainer');

const loadingMark = document.getElementById('loadingMark');

shareBtn.addEventListener("click", function () {
    document.body.style.overflow = "hidden";
    document.querySelector(".ModalBackground").classList.add("On")
})

modalCloseBtn.addEventListener("click", function () {
    document.body.style.overflow = "auto";
    document.querySelector(".ModalBackground").classList.remove("On")
})

modalBackground.addEventListener("click", function () {
    document.body.style.overflow = "auto";
    document.querySelector(".ModalBackground").classList.remove("On")
});


let isEventTriggered = false;
let page = 0;
let hasNext = true;

window.addEventListener('scroll', function () {
    if (isEventTriggered) return;

    let {scrollTop, scrollHeight, clientHeight} = document.documentElement;

    if (clientHeight + scrollTop >= scrollHeight) {
        isEventTriggered = true;
        loadMoreContents();
    }
});

moreContentsContainer.addEventListener('click', (e) => {
    if (e.target.classList.contains('OtherBtn') && hasNext) {
        e.target.remove();
        loadMoreContents();
    }
});

const loadMoreContents = async () => {
    await loadingMarkService.show(loadingMark);

    try {
        const r = await promotionListOfUserComponent(userId, promotionId, page);

        moreContentsBlock.insertAdjacentHTML("beforeend", r.html);

        if (r.hasMoreContents) {
            hasNext = r.hasMoreContents;
            moreContentsContainer.insertAdjacentHTML("beforeend", promotionLayouts.loadMoreBtn());
        }

        page++;
    } catch (e) {
        console.error(e)
    } finally {
        await loadingMarkService.hide(loadingMark);

        window.scrollTo({
            top: document.body.scrollHeight,
            behavior: 'smooth'
        });
    }
}