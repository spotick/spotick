import { requestAiResponse } from '../modules/fetch/openAiFetch.js'


const $chatbotBody = $('#chatbot-body');
const $chatbotBodyContent = $('#chatbot-body-content');
const $chatbotTextarea = $('#chatbot-textarea');
const chatLoadingMark = document.getElementById('chatLoadingMark');

$(document).ready(function () {
    // 채팅창 열기
    $('#chatbot-open').click(function () {
        $('#chatbot').show();
        $('#chatbot-open').hide();

        loadChatList();

        $chatbotBody.scrollTop($chatbotBody[0].scrollHeight);
    });

    // 채팅방 목록, 채팅 방 이동
    $('.show-chatBot').on('click', function () {
        $('.chat-room-detail-wrap').show();
        $('.chat-room-wrap').hide();

        $('.chat-prev').on('click', function () {
            $('.chat-room-detail-wrap').hide();
            $('.chat-room-wrap').show();

        })
    });

});

defaultChatMsg();

function defaultChatMsg() {
    let aiChatData = JSON.parse(sessionStorage.getItem('aiChatData'));
    let text = "안녕하세요 저는 Spotick 사이트의 챗봇입니다.";
    if (aiChatData) {
        text = aiChatData[aiChatData.length - 1].content;
    }
    $('.chat--bot-msg').text(text);
}

//세션스토리에 저장할 채팅내용들을 담을 배열 선언
let aiChatArr = [];

// 챗봇 비동기 통신
function sendMessage() {
    let message = $chatbotTextarea.val();

    if (message.length === 0) {
        return;
    }

    addUserMessage(message);
    $chatbotTextarea.val('');
    aiChatArr.push({role: 'user', content: message});

    sessionStorage.setItem('aiChatData', JSON.stringify(aiChatArr));
    requestAiResponse(aiChatArr, addBotMessage, chatLoadingMark);
    $chatbotBody.scrollTop($chatbotBody[0].scrollHeight);
}

// input 엔터 이벤트
$('.chat-room-detail-wrap').on('keydown', '#chatbot-textarea', function (e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        sendMessage();
    }
});

// 전송 버튼 클릭시
$('#chatbot-send').click(function() {
    sendMessage();
});


// 채팅창 닫기
$('.chat-screen-close').on('click', function () {
    $('#chatbot').hide();
    $('#chatbot-open').show();
});


// 유저 메세지 추가
function addUserMessage(message) {
    let htmlCode = `
        <div class="user-message message">
            <div class="message-text">${message}</div>
        </div>`;

    $chatbotBodyContent.append(htmlCode);
    $chatbotBody[0].scrollTop = $chatbotBody[0].scrollHeight;
}

// 챗봇 메세지 추가
function addBotMessage(message) {
    let htmlCode = `
        <div class="bot-message message">
            <div class="message-text">${message}</div>
        </div>
    `;

    $chatbotBodyContent.append(htmlCode);
    $chatbotBody[0].scrollTop = $chatbotBody[0].scrollHeight;
}


// 삭제버튼 띄우기
$('.chat--more-dot').on('click', function () {
    let exitBtn = $(this).siblings('.chat-room-exit-btn');

    if (exitBtn.is(':visible')) {
        exitBtn.hide();
    } else {
        exitBtn.show();
    }
});

// 채팅방 나가기 버튼 클릭시
$('.chat-room-exit-btn').on('click', function () {
    $(this).hide();
});

//챗봇 채팅창 보여주기
function loadChatList() {
    let text = '';

    //세션스토리지에 있는 값 가져오기
    let aiChatData = JSON.parse(sessionStorage.getItem('aiChatData'));

    text += `
        <div class="assistant message">
            <div class="message-text">
                안녕하세요 Spotick 사이트의 챗봇입니다. <br>
                저화 대화한 내용은 로그아웃하거나 브라우저를 종료하면 사라지게 됩니다
                무엇을 도와드릴까요?
            </div>
        </div>
    `;

    if (aiChatData) {
        aiChatData.forEach(c => {
            text += `
                <div class="${c.role === 'user' ? `user-message` : `assistant`} message">
                    <div class="message-text">${c.content}</div>
                </div>
            `;
        })
    }

    $chatbotBodyContent.html(text);
}
