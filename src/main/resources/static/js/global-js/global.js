import {getTimeGapFromToday} from '../modules/timeUtils.js';
import {loadingMarkService} from '../modules/loadingMark.js';


const notification = document.getElementById('notification');

// 로고 옆 컨텐츠 타입 설정
function toggleContent(type) {
    let currentType = document.getElementById('currentContent');

    if (type === "ticket") {
        currentType.classList.remove('type1');
        currentType.classList.add('type2');
        currentContent.innerHTML = '티켓';
    } else if (type === "place") {
        currentType.classList.remove('type2');
        currentType.classList.add('type1');
        currentContent.innerHTML = '장소';
    }
}

function getMainPageByType(type) {
    window.location.href = type === "place" ? "/place/list" : "/ticket/list";
}

function checkUrlType() {
    const currentUri = window.location.pathname;

    // pathname이 존재하지 않을 경우 /place/list이므로 place 반환
    if (currentUri === "/") {
        return "place";
    }

    const firstSegment = currentUri ? currentUri.split('/')[1] : null;

    // place인지 ticket인지 구분. 아니면 null 반환
    return (firstSegment === 'place' || firstSegment === 'ticket') ? firstSegment : null;
}


// 검색창 컨트롤
const searchBarOpen = document.getElementById('searchBarOpen');
const searchBarClose = document.getElementById('searchBarClose');
const searchBar = document.getElementById('searchBar');
const searchInput = document.getElementById('searchInput');
const searchInputDelete = document.getElementById('searchInputDelete');

searchBarOpen.addEventListener("click", () => {
    searchBarOpen.classList.add('hide')
    searchBar.classList.remove('hide')
    searchInput.focus()
})

searchBarClose.addEventListener("click", () => {
    searchBarOpen.classList.remove('hide')
    searchBar.classList.add('hide')
})

searchInput.addEventListener("input", () => {
    if (searchInput.value.trim() !== '') {
        searchInputDelete.classList.remove('hide');
    } else {
        searchInputDelete.classList.add('hide');
    }
})

searchInputDelete.addEventListener("click", () => {
    searchInput.value = '';
    searchInput.focus()
    searchInputDelete.classList.add('hide');
})

let randomTexts = [
    '어떤 장소를 찾으시나요?',
    '관심있는 행사가 있으신가요?',
    '어떤 행사를 계획하고 계신가요?'
];
let randomIndex = Math.floor(Math.random() * randomTexts.length);
searchInput.setAttribute('placeholder', randomTexts[randomIndex]);

// 헤더 유저메뉴, 알림창
const usermenuPopOver = document.querySelector(".hd-usermenu")
const notificationPopOver = document.querySelector('.hd-notification');

function toggleUsermenu() {
    notificationPopOver.classList.remove('show');
    usermenuPopOver.classList.toggle('show');
}

function toggleNotification() {
    usermenuPopOver.classList.remove('show');
    notificationPopOver.classList.toggle('show');
}

function toggleFooterHome() {
    const popover = document.querySelector('.fc-popover-homes');
    popover.classList.toggle('show');
}

let prevScrollpos = window.scrollY;
const header = document.getElementById("page-header");
const scrollThreshold = 200; // 스크롤다운 트리거 값

window.onscroll = function () {
    let currentScrollPos = window.scrollY;

    if (prevScrollpos > currentScrollPos) {
        header.classList.remove("hide");
    } else {
        if (currentScrollPos > scrollThreshold) {
            header.classList.add("hide");
        }
    }

    prevScrollpos = currentScrollPos;
};

const notificationBody = document.querySelector('.hd-notification-body');
const notificationReload = document.getElementById('notificationReload');
const notificationCount = document.getElementById('notificationCount');
const notificationLoadingMark = document.getElementById('notificationLoadingMark');

const notificationService = (function () {
    let isLoading = false;

    function requestNotificationList() {
        if (!isLoading) {
            isLoading = true;

            notificationBody.innerHTML = '';

            loadingMarkService.show(notificationLoadingMark);

            setTimeout(() => {
                fetch("/notice/api/list", {
                    method: 'GET'
                })
                    .then(response => {
                        isLoading = false;
                        loadingMarkService.hide(notificationLoadingMark);

                        if (response.ok) {
                            return response.json();
                        } else {
                            throw response
                        }
                    })
                    .then(res => {
                        if (res.data.length > 0) {
                            loadNotificationList(res.data);
                        } else {
                            loadNoContent();
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            }, 500);
        }
    }

    function loadNotificationList(data) {
        if (data.length === 0) {
            return;
        }

        let anyUnread = false;

        notificationCount.innerHTML = data.length

        data.forEach(notification => {
            let html = `
                <div class="hdn-item" id="${notification.noticeId}" link="${notification.link}">
                    <div style="display: flex; justify-content: space-between;">
                        <div class="hdn-title">${notification.title}</div>
                        <div class="hdn-date">${getTimeGapFromToday(notification.createdDate)}</div>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <div class="hdn-content">${notification.content}</div>
                        <button class="hdn-button">삭제하기</button>
                    </div>
                </div>
            `;

            if (notification.noticeStatus === "UNREAD") anyUnread = true;

            notificationBody.insertAdjacentHTML("beforeend", html);
        });

        if (anyUnread) notification.classList.add('alarm');

        document.querySelectorAll('.hdn-button').forEach(button => {
            button.addEventListener('click', (e) => {
                const hdnItem = e.target.closest('.hdn-item');

                if (hdnItem) {
                    const id = hdnItem.id;
                    notificationService.requestUpdateStatus(id, "deleted");
                }
            });
        });

        document.querySelectorAll('.hdn-item').forEach(item => {
            item.addEventListener('click', (e) => {
                if (!e.target.classList.contains('hdn-button')) {
                    window.location.href = item.getAttribute('link');
                }
            })
        })
    }

    function requestUpdateStatus(noticeId, status) {
        const condition = status === "read" ? "READ" : "DELETED";

        const requestDto = {
            noticeId: noticeId,
            noticeStatus: condition
        }

        fetch("/notice/api/updateStatus", {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestDto),
        })
            .then(response => {
                if (response.ok) {
                    deleteNotificationHtml(noticeId);
                } else {
                    throw response
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function deleteNotificationHtml(id) {
        const targetNotification = notificationBody.querySelector(`.hdn-item[id="${id}"]`);

        targetNotification.remove();

        let currentCount = parseInt(notificationCount.innerHTML);
        currentCount--;
        notificationCount.innerHTML = currentCount.toString();

        if (notificationBody.children.length === 0) {
            notificationCount.innerHTML = '';
            notification.classList.remove('alarm');
            loadNoContent();
        }
    }

    function loadNoContent() {
        notificationBody.innerHTML = `
            <div class="hdn-notification-none">
                <span>알림이 없습니다.</span>
            </div>
        `;
    }

    return {
        requestNotificationList: requestNotificationList,
        loadNotificationList: loadNotificationList,
        requestUpdateStatus: requestUpdateStatus
    }
})();


///////////////////////////////////////////////////////////////////////////
window.onload = function () {
    const type = checkUrlType();
    if (type === null) {
        return;
    }
    toggleContent(type);
}

document.querySelectorAll('.hc-content-type').forEach(button => {
    button.addEventListener('click', async function () {
        const type = this.getAttribute('main-type');

        await toggleContent(type);

        await new Promise(resolve => setTimeout(resolve, 300));

        await getMainPageByType(type);
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const usermenuElement = document.getElementById('usermenu');
    if (usermenuElement) {
        usermenuElement.addEventListener('click', toggleUsermenu);
    }

    if (notification) {
        notification.addEventListener('click', toggleNotification);
    }

    if (notificationReload) {
        notificationReload.addEventListener('click', notificationService.requestNotificationList);
    }
});

document.getElementById('footerHome').addEventListener('click', toggleFooterHome)

document.addEventListener("DOMContentLoaded", function () {
    if (notification) {
        notificationService.requestNotificationList();
    }
});

// 장소 검색
$('#searchInput').on('keyup',function (e){
    if (e.code == 'Enter'){
        e.preventDefault();
        let keyword = $(this).val();
        location.href = `/place/list?keyword=${keyword}`;
    }
});



