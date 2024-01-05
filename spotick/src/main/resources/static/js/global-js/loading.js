function showLoadingScreen() {
    const loadingHtml = `<div id="loadingBackground">
                                    <div id="loadingImg">
                                        <img src="../../../static/imgs/loading.svg">
                                    </div>
                                </div>`;

    document.body.insertAdjacentHTML('afterbegin', loadingHtml);
}

function closeLoadingScreen() {
    let loadingHtml = document.getElementById('loadingBackground');

    document.body.removeChild(loadingHtml);
}