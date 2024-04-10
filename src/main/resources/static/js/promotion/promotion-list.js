var topSwiper = new Swiper(".top-swiper", {
    slidesPerView: 4,
    slidesPerGroup: 4,
});

var swiper = new Swiper(".category-swiper", {
    width:800,
    slidesPerView: 10,
    slidesPerGroup: 10,
});

function swiperPositionSet(el, Xposition, index) {
    $(el).attr(
        'style',
        'transform:translate3d(' + Xposition[index] + 'px ,0px,0px); transition-duration: 300ms;'
    );
}

function mainSlideChangeStart () {
    var swiper_position_X = [0, -190, -380, -520];
    swiperPositionSet(
        '.category-swiper .swiper-wrapper',
        swiper_position_X,
        mySwiperMainNav.realIndex
    );
}

window.onload = function() {
    console.log('실행')
    // localStorage에서 'visited' 키를 확인하여 값이 없으면 alert 창을 띄우고, 그 값을 true로 설정합니다.
    if (!localStorage.getItem('visited')) {
        alert('처음 방문하셨습니다!');
        localStorage.setItem('visited', true);
    }
}

let filterBtn = document.querySelector(".FilterBtn button")
let filterModal = document.querySelector(".FilterModal")
let modalCloseBtn = document.querySelector(".FilterModalCloseBtn")
let modalSaveBtn = document.querySelector(".FilterModalFooterSaveBtn")
let modalResetBtn = document.querySelector(".FilterModalFooterResetBtn")
let modalCategoryBtn = document.querySelectorAll(".FilterModalCategoryChild")
let CategoryBtn = document.querySelectorAll(".Category")


filterBtn.addEventListener("click", function () {
    // 모달 보이기
    filterModal.style.display = "block";
    document.body.style.overflow = "hidden";
    // CategoryList Category 중에서 "On" 클래스가 있는지 확인
    const categoryOn = document.querySelector('.CategoryList.Category.On');

    // "On" 클래스가 있는 경우 FilterModalCategoryChild에 "On" 클래스 추가
    if (categoryOn && !categoryOn.classList.contains('CategoryList.Category.All')) {
        const categoryText = categoryOn.textContent.trim();
        document.querySelectorAll('.FilterModalCategoryChild').forEach(function (button) {
            if (button.textContent.trim() === categoryText) {
                button.classList.add('On');
            } else {
                button.classList.remove('On');
            }
        });
    }
});

modalCloseBtn.addEventListener("click", function(){
    filterModal.style.display = "none";
    document.body.style.overflow = "auto";
})

modalSaveBtn.addEventListener("click", function(){
    filterModal.style.display = "none";
    document.body.style.overflow = "auto";
    // CategoryList Category 버튼의 On 클래스 제거
    document.querySelectorAll('.CategoryList.Category.On').forEach(function (button) {
        button.classList.remove('On');
    });

    // FilterModalCategoryChild 중에서 On 클래스를 가진 버튼 찾아서 CategoryList Category에 On 클래스 추가
    const isFilterOn = document.querySelector('.FilterModalCategoryChild.On');

    if (isFilterOn) {
        document.querySelectorAll('.FilterModalCategoryChild.On').forEach(function (button) {
            const categoryText = button.textContent.trim();
            document.querySelectorAll('.CategoryList.Category').forEach(function (categoryButton) {
                if (categoryButton.textContent.trim() === categoryText) {
                    categoryButton.classList.add('On');
                }
            });
        });
    } else {
        // On 클래스가 없는 경우 CategoryList Category 중에서 "전체"에 On 클래스 추가
        document.querySelectorAll('.CategoryList.Category').forEach(function (categoryButton) {
            if (categoryButton.textContent.trim() === '전체') {
                categoryButton.classList.add('On');
            } else {
                categoryButton.classList.remove('On');
            }
        });
    }
})

modalCategoryBtn.forEach(function(button) {
    button.addEventListener("click", function() {
        modalCategoryBtn.forEach(function(btn) {
            // 클릭한 버튼을 제외한 다른 버튼에서 "On" 클래스 제거
            if (btn !== button) {
                btn.classList.remove("On");
            }
        });

        // 클릭한 버튼에 "On" 클래스 토글
        button.classList.toggle("On");
    });
});

CategoryBtn.forEach(function(button) {
    button.addEventListener("click", function(){
        if (!button.classList.contains("On")) {
            CategoryBtn.forEach(function(btn) {
                btn.classList.remove("On");
            });
            button.classList.add("On");
        }
    })
});

modalResetBtn.addEventListener("click", function() {
    modalCategoryBtn.forEach(function(button) {
        button.classList.remove("On");
    });
});
