import {promotionListOfUserComponent} from "../components/promotion/promotionComponents.js";
import {showGlobalSelection, showGlobalDialogue} from "../global-js/global-modal.js"
import {promotionService} from "../services/promotion/promotionService.js";
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

const isLoggedIn = document.getElementById('isLoggedIn');

const countIndicator = document.getElementById('count');


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

const likeBtn = document.getElementById('likeBtn');

likeBtn.addEventListener('click', () => {
    if (isLoggedIn.value === 'false') {
        const selection = confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?");
        if (selection) {
            location.href = '/user/login';
            return;
        } else {
            return;
        }
    }

    const status = likeBtn.getAttribute('data-status');

    promotionService.likeRequest(promotionId, status)
        .then((r) => {
            likeBtn.setAttribute('data-status', r);

            let curVal = parseInt(countIndicator.textContent);

            if (r === true) {
                countIndicator.textContent = (++curVal).toString();
            } else {
                countIndicator.textContent = (--curVal).toString();
            }

            changeLike(likeBtn, r);
        });
});

function changeLike(btn, status) {
    // status에 따라서 클래스 변경
    const off = btn.children[0];
    const on = btn.children[1];

    if (status) {
        off.classList.add('none');
        on.classList.remove('none')
    } else {
        off.classList.remove('none');
        on.classList.add('none')
    }
}

document.getElementById('delete')?.addEventListener('click', () => {
    const message = "게시글을<br>삭제하시겠습니까?";
    showGlobalSelection(message, deleteBoard);
});

const deleteBoard = () => {
    promotionService.deleteBoard(promotionId)
        .then(r => {

            if (r.ok === true) {
                const message = "게시글이 삭제 되었습니다.";

                showGlobalDialogue(message, () => {
                    window.location.href = "/promotion";
                });
            }
        })
        .catch(() => {
            showGlobalDialogue("오류가 발생했습니다<br>나중에 시도해주세요.");
        });
}