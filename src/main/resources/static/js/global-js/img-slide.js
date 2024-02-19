// 한칸당 이미지가 차지하게 되는 width(px)
const IMG_WIDTH = 500;


const imgSlideService = (function () {

    // 슬라이드 배너를 생성, sources: 이미지파일 배열; ul: 슬라이드 배너가 들어갈 html
    function setImgSlides(sources, ul, fileDatas) {
        let slideHtml = '';
        let index = 0;

        if (fileDatas) {
            fileDatas.forEach(f => {
                let reader = new FileReader();

                reader.onload = e => {
                    // html은 상황에 맞게 설정
                    slideHtml = `
                        <li class="imgSlide-item" index="${index}">
                            <img src="${e.target.result}">
                            <button class="imgSlide-delete-btn" type="button">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </li>
                    `;
                    ul.insertAdjacentHTML("beforeend", slideHtml);
                }
                index++;
                reader.readAsDataURL(f);
            })
            ul.slideLength = fileDatas.length;
        } else {
            sources.forEach(source => {
                // html은 상황에 맞게 사용할 것
                slideHtml +=
                    `<li class="imgSlide-item" index="${index}" data-id="${source.placeFileId}">
                        <img src="/file/display?fileName=${source.uploadPath}/${source.uuid}_${source.fileName}">
                        <button class="imgSlide-delete-btn" type="button">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </li>`;

                index++;
            })
            // 슬라이드 랩에 프로퍼티로 slide의 총갯수 저장
            ul.slideLength = sources.length;
            ul.innerHTML = slideHtml;
        }

        // 현재페이지 프로퍼티 추가
        ul.currentPage = 0;
    }

    function addSlide(source, ul, resetPaginationHtml) {
        ul.slideLength++;

        let slideHtml =
            `<li class="imgSlide-item" index="${ul.slideLength}" data-id="${source.placeFileId}">
                <img src="/file/display?fileName=${source.uploadPath}/${source.uuid}_${source.fileName}">
                <button class="imgSlide-delete-btn">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </li>`

        ul.insertAdjacentHTML("beforeend", slideHtml);

        if (resetPaginationHtml) {
            imgSlideService.setSlidePagination(ul.slideLength, resetPaginationHtml)
        }
    }

    /*
    * 페이지 네이션 설정
    *
    * length : 슬라이드의 갯수
    * containerHtml : 페이지네이션 버튼이 들어가는 컨테이너
    * (옵션)maxLength : 페이지네이션 버튼이 초과할 경우를 초과할 경우를 대비해 설정.
    * (옵션)optionalContainerHtml : 페이지네이션 버튼이 초과할 경우를 대비해 배치할 대체 컨테이너, 없을 시 containerHtml로
    * */
    function setSlidePagination(length, containerHtml, index = 0) {
        let paginationHtml = '';

        for (let i = 0; i < length; i++) {
            const activeClass = i === index ? 'active' : '';

            paginationHtml += `<button class="imgSlide-pagination-btn ${activeClass}" index="${i}" type="button"></button>`;
        }

        containerHtml.innerHTML = paginationHtml;

    }

    function moveSlide(direction, ul, callback) {
        if (direction === "right") {
            if (ul.currentPage === ul.slideLength - 1) {
                return;
            }
            ul.currentPage++;
        } else if (direction === "left") {
            if (ul.currentPage === 0) {
                return;
            }
            ul.currentPage--;
        }

        const newPosition = -ul.currentPage * IMG_WIDTH;
        ul.style.transform = `translateX(${newPosition}px)`;

        if (callback) {
            callback(ul.currentPage, ul);
        }
    }

    function getSlidePage(page, ul, callback) {
        ul.currentPage = page;
        ul.style.transform = `translateX(-${page * IMG_WIDTH}px)`

        if (callback) {
            callback();
        }
    }

    return {
        setImgSlides: setImgSlides,
        addSlide: addSlide,
        setSlidePagination: setSlidePagination,
        moveSlide: moveSlide,
        getSlidePage: getSlidePage
    }
})();

