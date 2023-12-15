$('.all-check').on('click', function() {
    $(this).toggleClass('on');

    if($(this).hasClass('on')){
        $('.check').addClass('on');
    }else{
        $('.check').removeClass('on');
    }
});

$('.check').on('click',function(){
    $(this).toggleClass('on');

    if(isAllCheck()){
        $('.all-check').addClass('on');
    }else{
        $('.all-check').removeClass('on');
    }
});


function isAllCheck(){
    for (let i = 0; i < $('.check').length; i++) {
        if(!$('.check')[i].classList.contains('on')){
            return false;
        }
    }
    return true;
}

















