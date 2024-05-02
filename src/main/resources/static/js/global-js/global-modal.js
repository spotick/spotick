const modalWrap = document.getElementById('modal');

const globalDialogue = document.getElementById('globalDialogue')
const globalDialogContent = document.getElementById('gdCont');
const globalDialogueButtonWrap = document.getElementById('gdButtonWrap');

const globalSelection = document.getElementById('globalSelection');
const globalSelectionContent = document.getElementById('gsCont');
const globalSelectionButtonWrap = document.getElementById('gsButtonWrap');


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
    globalDialogueButtonWrap.innerHTML = ``;

    if (closeOthers) {
        const showElements = modalWrap.querySelectorAll('.show');
        showElements.forEach(element => {
            element.classList.remove('show');
        });
    }

    modalWrap.classList.add('show');
    globalDialogue.classList.add('show');

    globalDialogContent.innerHTML = dialogue;

    const confirmButton = document.createElement('button');
    confirmButton.type = "button";
    confirmButton.classList.add('gs-select-btn', 'conf');
    confirmButton.textContent = "확인";

    globalDialogueButtonWrap.appendChild(confirmButton);

    if (callback) {
        confirmButton.addEventListener('click', callback);
        return;
    }

    confirmButton.addEventListener('click', closeGlobalDialogue);
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
    // 이벤트 중첩 효과를 제거하기 위해 버튼들을 전부 제거한다.
    globalSelectionButtonWrap.innerHTML = ``;

    // 기본적으로 이 모달 창이 켜지면 다른 모달창은 전부 꺼지게 된다.
    // 그러나 closeOthers를 false 설정하면 이 모달창이 켜져도 꺼지지 않게 된다.
    if (closeOthers) {
        const showElements = modalWrap.querySelectorAll('.show');
        showElements.forEach(element => {
            element.classList.remove('show');
        });
    }

    modalWrap.classList.add('show');
    globalSelection.classList.add('show');

    globalSelectionContent.innerHTML = dialogue;

    // 버튼 태그를 만든다.
    const confirmButton = document.createElement('button');
    confirmButton.type = "button";
    confirmButton.classList.add('gs-select-btn', 'conf');
    confirmButton.textContent = "확인";

    const cancelButton = document.createElement('button');
    cancelButton.type = "button";
    cancelButton.classList.add('gs-select-btn');
    cancelButton.textContent = "취소";

    // 버튼 태그를 html 상에 삽입한다.
    globalSelectionButtonWrap.appendChild(cancelButton);
    globalSelectionButtonWrap.appendChild(confirmButton);

    // 이벤트리스너를 새롭게 걸어준다. 이미 초기화 하여 버튼태그를 새롭게 만들어 html상에 삽입했으므로,
    // 이벤트리스너 중첩 효과는 없게 된다.
    confirmButton.addEventListener('click', confirmCallback);

    if (cancelCallback) {
        cancelButton.addEventListener('click', cancelCallback);
        return;
    }

    cancelButton.addEventListener('click', closeGlobalSelection);
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