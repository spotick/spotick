// 아이템 이미지 슬라이드
// 모든 OneItemContainer에 대해 반복
document.querySelectorAll('.OneItemContainer').forEach(function (container, index) {
    // 현재 container의 ImageSwiper 클래스를 가진 요소를 찾음
    var swiperContainer = container.querySelector('.ImageSwiper');

    // Swiper 초기화
    var swiper = new Swiper(swiperContainer, {
        pagination: {
            el: swiperContainer.querySelector('.swiper-pagination'),
            clickable: true,
        },
        navigation: {
            nextEl: swiperContainer.querySelector('.RightBtn'),
            prevEl: swiperContainer.querySelector('.LeftBtn'),
        },
        on: {
            slideChange: function () {
                handleSlideChange(this);

            },
        },
    });

    // 슬라이드 index최대값
    page(swiper);


    // 초기 페이지네이션 업데이트
    updatePagination(swiper);
});

function handleSlideChange(swiper) {
    var snapIndexElement = swiper.el.querySelector(".snapIndex");
    if (snapIndexElement) {
        snapIndexElement.textContent = swiper.snapIndex + 1;
    }
}

function page(swiper){
    var pageIndex = swiper.el.querySelector(".slideIndex");
    if (pageIndex) {
        pageIndex.textContent = swiper.slides.length;
    }
    // 슬라이드가 1개일 경우 hover 없애기
    if(swiper.slides.length === 1){
        // 부모 노드를 탐색하면서 hover 클래스를 가진 부모를 찾습니다.
        let parentWithHoverClass = null;
        let parentNode = pageIndex.parentElement;
        let parentContainer = pageIndex.parentElement.parentElement;
        console.log(parentContainer)

        while (parentNode) {
            if (parentNode.classList.contains("hover")) {
                parentWithHoverClass = parentNode;
                break;
            }
            parentNode = parentNode.parentElement;
        }

        if (parentWithHoverClass) {
            parentContainer.style.display = "none";
            parentWithHoverClass.classList.remove("hover");
        }
    }

}

function updatePagination(swiper) {
    swiper.pagination.update(); // Swiper에게 페이지네이션을 다시 그리도록 알림
}