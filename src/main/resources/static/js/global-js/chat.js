
// import * as chatBot from '../module/openAi.js';

$(document).ready(function () {
    // 채팅창 열기
    $('#chatbot-open').click(function () {
        $('#chatbot').show();
        $('#chatbot-open').hide();
        $('.chat-room-wrap').show();
    });

    // 채팅방 목록, 채팅 방 이동
    $('.show-chatBot').on('click', function () {
        $('.chat-room-detail-wrap').show();
        showChatBotRoom();
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
    let text = "안녕하세요 저는 Happy Pet's Day 사이트의 챗봇입니다.";
    if (aiChatData) {
        text = aiChatData[aiChatData.length - 1].content;
    }
    $('.chat--bot-msg').text(text);
}

//세션스토리에 저장할 채팅내용들을 담을 배열 선언
let aiChatArr = [];

// 챗봇 비동기 통신
function sendMessage() {
    let message = $('#chatbot-textarea').val();
    if (message.length === 0) {
        return;
    }
    addUserMessage(message);
    $('#chatbot-input').val('');
    aiChatArr.push({role: 'user', content: message});

    sessionStorage.setItem('aiChatData', JSON.stringify(aiChatArr));
    chatBot.sendMessage(aiChatArr, addBotMessage,defaultChatMsg);
}

// input칸 엔터 이벤트
$('.chat-room-detail-wrap').on('keypress', '#chatbot-textarea', function (e) {
    if (e.code === 'Enter') {
        sendMessage();
    }
});


// 채팅창 닫기
$('.chat-room-detail-wrap').on('click', '.chat-screen-close', function () {
    $('#chatbot').hide();
    $('#chatbot-open').show();
});

$('.chat-screen-close').on('click', function () {
    $('#chatbot').hide();
    $('#chatbot-open').show();
});


// 유저 메세지 추가
function addUserMessage(message) {
    let htmlCode = `<div class="user-message message">
         <div class="message-text">${message}</div>
       </div>`;

    $('.chatbot-body').append(htmlCode);
    $('.chatbot-body')[0].scrollTop = $('.chatbot-body')[0].scrollHeight;// 스크롤 하단으로 이동
}

// 챗봇 메세지 추가
function addBotMessage(message) {
    let htmlCode = `<div class="bot-message message">
         <div class="message-text">${message}</div>
       </div>`;

    $('.chatbot-body').append(htmlCode);
    $('.chatbot-body')[0].scrollTop = $('.chatbot-body')[0].scrollHeight;// 스크롤 하단으로 이동
}


// 삭제버튼 띄우기
$('.chat--more-dot').on('click', function (e) {
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
function showChatBotRoom() {
    let text = '';

    //세션스토리지에 있는 값 가져오기
    let aiChatData = JSON.parse(sessionStorage.getItem('aiChatData'));

    text += `
    <div id="chatbot-header" class="chatbot-header">
                <div class="close-box">
                    <button type="button" id="chat-prev" class="chatbot-close chat-prev">
                        <i class="fa-solid fa-arrow-left"></i>
                    </button>
                    <h4 class="chat-room-name">챗봇</h4>
                    <button type="button" class="chatbot-close chat-screen-close">
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                </div>
            </div>
            <div id="chatbot-body" class="chatbot-body">
                <div class="bot-message message">
                    <div class="message-text">
                        안녕하세요 저는 Happy Pet's Day 사이트의 챗봇입니다. <br>
                        저화 대화한 내용은 로그아웃하거나 브라우저를 종료하면 사라지게 됩니다
                        무엇을 도와드릴까요?
                    </div>
                </div>`;

    if (aiChatData) {
        aiChatData.forEach(c => {
            text += `
                        <div class="${c.role === 'user' ? `user-message` : `bot-message`} message">
                             <div class="message-text">${c.content}</div>
                        </div>
                        `;
        })
    }

    text += `</div>
            <div id="chatbot-footer" class="chatbot-footer">
                <input type="text" id="chatbot-input" placeholder="궁금한것을 물어보세요!"/>
                <button id="chatbot-send">전송</button>
            </div>
    `;
    $('.chat-room-detail-wrap').html(text);
}
