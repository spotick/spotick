const savedImgs = document.getElementById('savedImgs')
const savedImgPagination = document.getElementById('savedImgPagination');

$('#savedLeft').click(() => {
    imgSlideService.moveSlide("left", savedImgs);
});

$('#savedRight').click(() => {
    imgSlideService.moveSlide("right", savedImgs);
});


$(document).on('click', '.imgSlide-delete-btn', function() {
    let liElement = $(this).closest('.imgSlide-item');
    let index = liElement.index();

    liElement.remove();
    $('#saveList').find('input[name="saveFileIdList"]').eq(index).remove();

    if (savedImgs.currentPage === savedImgs.slideLength - 1) {
        imgSlideService.moveSlide("left", savedImgs);
    }

    savedImgs.slideLength--;
});

$('#hrefBack').click(function() {
    window.history.back();
});