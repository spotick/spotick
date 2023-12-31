
// 해당 장소의 기본 인원
let defaultPeople =4;

$('.visitor-ctr-box button').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= Number($visitors.val());
    let number = visitNum+Number($(this).val());
    $visitors.val(number>99?99:number);
    updateSelectedPeople()
});

$('.plus').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= $visitors.val();
    if(visitNum>=99){
        $visitors.val(99);
        return;
    }
    $visitors.val(++visitNum);
    updateSelectedPeople()
});

$('.minus').on('click',function (){
    let $visitors = $('.visitors');
    let visitNum= $visitors.val();
    if(visitNum<=1){
        $visitors.val(1);
        return;
    }
    $visitors.val(--visitNum);
    updateSelectedPeople()
});

function updateSelectedPeople(){
    let selectedCount = $('.visitors').val();
    let additionMsg = selectedCount>defaultPeople? ` (${selectedCount-defaultPeople}명 추가)`:``;

    $('.selected-count').text(`${selectedCount}명${additionMsg}`);
}























