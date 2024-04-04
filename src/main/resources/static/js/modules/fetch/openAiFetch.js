import {loadingMarkService} from '../loadingMark.js';

export function requestAiResponse(aiChatArr, callback, loadingMark) {
    if (loadingMark) {
        loadingMarkService.show(loadingMark);
    }

    fetch(`/chat/question`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(aiChatArr),
    })
        .then(response => {
            if (loadingMark) {
                loadingMarkService.hide(loadingMark);
            }

            if (response.ok) {
                return response.json();
            }
        })
        .then(data => {
            let message = data.choices[0].message.content;

            aiChatArr.push({role: 'assistant', content: message});
            sessionStorage.setItem('aiChatData', JSON.stringify(aiChatArr));
            callback(message);
        })
        .catch((error) => {
            console.error(error);
            let message = '챗봇과 연결이 되지 않았습니다. 나중에 다시 시도해 주세요';
            aiChatArr.push( {role:'assistant',content:message}  );

            sessionStorage.setItem('aiChatData', JSON.stringify(aiChatArr));
            callback(message);
        });
}