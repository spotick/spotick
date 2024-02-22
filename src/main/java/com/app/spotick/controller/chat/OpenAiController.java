package com.app.spotick.controller.chat;

import com.app.spotick.service.chat.OpenAiService;
import com.app.spotick.vo.GptChatVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class OpenAiController {
    private final OpenAiService openAiService;

    @PostMapping("/question")
    public Mono<Map> question(/*@RequestBody List<ChatVo> list*/) {
        List<GptChatVo> list = new ArrayList<>();

        GptChatVo chat = GptChatVo.builder()
                .role("user")
                .content("안녕 chatGpt")
                .build();

        list.add(chat);

        return openAiService.getGptMessage(list);
    }
}
