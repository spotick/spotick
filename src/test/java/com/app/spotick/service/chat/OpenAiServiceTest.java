package com.app.spotick.service.chat;

import com.app.spotick.vo.GptChatVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class OpenAiServiceTest {

    @Autowired
    private OpenAiService openAiService;

    @Test
    public void testGptConnection() {
        List<GptChatVo> list = new ArrayList<>();

        GptChatVo chat = GptChatVo.builder()
                .role("user")
                .content("안녕 chatGpt")
                .build();

        list.add(chat);

        Mono<Map> gptMessage = openAiService.getGptMessage(list);

        System.out.println("gptMessage = " + gptMessage);
    }

}