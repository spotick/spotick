const modalWrap = document.getElementById('modal');

const globalDialogue = document.getElementById('globalDialogue')
const globalDialogContent = document.getElementById('gdCont');
const globalDialogConfirm = document.getElementById('gdConf');

const globalSelection = document.getElementById('globalSelection');
const globalSelectionContent = document.getElementById('gsCont');
const globalSelectionCancel = document.getElementById('gsCancel');
const globalSelectionConfirm = document.getElementById('gsConf');


modalWrap.addEventListener('click', (e) => {
    if (e.target === modalWrap) {
        closeEveryModal();
    }
});


/*
* global dialogue 모달창을 띄어주는 함수
* @param {String} dialogue - global dialogue의 내용을 할당시켜준다.
* @param {function} callback - 확인버튼을 클릭할 시 실행될 함수를 할당 시켜준다;
*                               할당하지 않을 시 디폴트로 closeGlobalDialogue()를 할당한다.
* @Param {boolean} closeOthers - default=true global dialogue 창을 띄울 때 다른 모달창들을 닫을 것인지 결정한다.
* */
export const showGlobalDialogue = (dialogue, callback, closeOthers = true) => {
    if (closeOthers) {
        const showElements = modalWrap.querySelectorAll('.show');
        showElements.forEach(element => {
            element.classList.remove('show');
        });
    }

    modalWrap.classList.add('show');
    globalDialogue.classList.add('show');

    globalDialogContent.innerHTML = dialogue;

    if (callback) {
        globalDialogConfirm.addEventListener('click', callback);
        return;
    }

    globalDialogConfirm.addEventListener('click', closeGlobalDialogue);
}

/*
* global dialogue 모달창을 닫아주는 함수
* */
export const closeGlobalDialogue = () => {
    modalWrap.classList.remove('show');
    globalDialogue.classList.remove('show');
}

/*
* global selection 모달창을 띄어주는 함수
* @param {String} dialogue - global selection의 내용을 할당시켜준다.
* @param {function} confirmCallback - Nonnull 확인버튼을 클릭할 시 실행될 함수를 할당 시켜준다.
* @param {function} cancelCallback - Nullable 취소버튼을 클릭할 시 실행될 함수를 할당 시켜준다;
*                                    없을 시 디폴트로 closeGlobalSelection()를 할당한다.
* @Param {boolean} closeOthers - default=true global selection 창을 띄울 때 다른 모달창들을 닫을 것인지 결정한다.
* */
export const showGlobalSelection = (dialogue, confirmCallback, cancelCallback, closeOthers = true) => {
    if (closeOthers) {
        const showElements = modalWrap.querySelectorAll('.show');
        showElements.forEach(element => {
            element.classList.remove('show');
        });
    }

    modalWrap.classList.add('show');
    globalSelection.classList.add('show');

    globalSelectionContent.innerHTML = dialogue;

    globalSelectionConfirm.addEventListener('click', confirmCallback);

    if (cancelCallback) {
        globalSelectionCancel.addEventListener('click', cancelCallback);
        return;
    }

    globalSelectionCancel.addEventListener('click', closeGlobalSelection);
}

/*
* global selection 모달창을 닫아주는 함수
* */
export const closeGlobalSelection = () => {
    modalWrap.classList.remove('show');
    globalSelection.classList.remove('show');
}

/*
* 모든 모달창을 닫아주는 함수
* @param {function} callback - Nullable 모달창을 닫을 시 특정함수를 실행시켜야 한다면 callback에 함수 할당
* */
export const closeEveryModal = (callback) => {
    modalWrap.classList.remove('show');

    const showElements = modalWrap.querySelectorAll('.show');
    showElements.forEach(element => {
        element.classList.remove('show');
    });

    if (callback) {
        callback();
    }
}

export const showCustomModal = (custom) => {
    const showElements = modalWrap.querySelectorAll('.show');
    showElements.forEach(element => {
        element.classList.remove('show');
    });

    modalWrap.classList.add('show');
    custom.classList.add('show');
}

export const closeSingleModal = (type, custom) => {
    let element;
    switch (type) {
        case "gd":
            element = globalDialogue;
            break;
        case "gs":
            element = globalSelection;
            break;
        case "custom":
            element = custom;
            break;
    }
    element.classList.remove('show');
}