var topSwiper = new Swiper(".top-swiper", {
    pagination: {
        el: ".swiper-pagination",
        clickable: true // 페이지네이션을 클릭 가능하게 만듭니다
    }
});

const bookmarkBtn = document.querySelector(".SideBookMark");
const bookmarkImg = document.querySelector(".SideBookMark img");

const shareBtn = document.querySelector(".SideMenuLink")
const modalCloseBtn = document.querySelector(".ShareModalCloseBtn")
const modalBackground = document.querySelector(".ModalBackground")

bookmarkBtn.addEventListener("click", function (){
    bookmarkImg.src = bookmarkImg.src.includes('filled') ? '../../static/imgs/bookmark_gray014_24.svg' : '../../static/imgs/bookmark_filled_gray014_24.svg' ;
})

shareBtn.addEventListener("click",function (){
    document.body.style.overflow = "hidden";
    document.querySelector(".ModalBackground").classList.add("On")
})

modalCloseBtn.addEventListener("click",function (){
    document.body.style.overflow = "auto";
    document.querySelector(".ModalBackground").classList.remove("On")
})

modalBackground.addEventListener("click",function (){
    document.body.style.overflow = "auto";
    document.querySelector(".ModalBackground").classList.remove("On")
})