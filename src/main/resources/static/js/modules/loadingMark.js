export const loadingMarkService = (function () {

    async function show(loadingMark) {
        loadingMark.classList.add('show');
    }

    function hide(loadingMark) {
        loadingMark.classList.remove('show')
    }

    return {
        show:show,
        hide:hide
    }
})();