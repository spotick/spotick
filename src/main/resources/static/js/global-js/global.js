// 로고 옆 컨텐츠 타입 설정
// import {data} from "../map/map";

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
const usermenu = document.querySelector(".hd-usermenu")
const notification = document.querySelector(".hd-notification")

function toggleUsermenu() {
    notification.classList.remove('show');
    usermenu.classList.toggle('show');
}

function toggleNotification() {
    usermenu.classList.remove('show');
    notification.classList.toggle('show');
}

function toggleFooterHome(button) {
    let popover = button.nextElementSibling;
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

// http://localhost:10000/mypage/places/inquiries/100 를 예시로 현재 링크에서 맨 끝자리의 변수를 찾아내서 반납해주는 함수
// 링크에서 숫자를 찾아낼 시 String 반환 <> 없을시 null반환
function extractVariableFromURL() {
    let variableValue = window.location.href.match(/\/(\d+)$/);

    return variableValue ? variableValue[1] : null;
}


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