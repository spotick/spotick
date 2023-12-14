// 로고 옆 컨텐츠 타입 설정
function toggleContent(index) {
    let currentType = document.getElementById('currentContent');

    if (index == 1) {
        currentType.classList.remove('type1');
        currentType.classList.add('type2');
        currentContent.innerHTML = '티켓팅';
    } else {
        currentType.classList.remove('type2');
        currentType.classList.add('type1');
        currentContent.innerHTML = '장소';
    }
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