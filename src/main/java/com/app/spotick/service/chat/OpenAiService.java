package com.app.spotick.service.chat;

import com.app.spotick.vo.GptChatVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {
    private final WebClient webClient;

    public OpenAiService(@Value("${gpt.api}") String apiKey) {
        String endpoint = "https://api.openai.com/v1/chat/completions";
        this.webClient = WebClient.builder()
                .baseUrl(endpoint)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    private List<GptChatVo> messageSetting(List<GptChatVo> list) {
        GptChatVo system = new GptChatVo();
        system.setRole("system");
        system.setContent("You are a customer service representative for our site, capable of answering anything about our website. The name of our site is Happy Pet's Day, and we provide various services for pets. The main features include finding someone to walk your pet with, support and reservation for pet sitters, pet recommendations, and providing information related to pet adoption.\n" +
                          "If a question comes in about the features or services on our site, based on the previous question, make the user able to use the service right away by providing them with:\n" +
                          "1. <a href=\"/stroll/list\" style=\"font-weight: bolder; font-size: 20px; color: #68a5fe;\">산책메이트 구하기</a> \n" +
                          "2. <a href=\"/sitter/list\" style=\" font-weight: bolder; font-size: 20px; color: #68a5fe;\">펫시터 예약</a> \n" +
                          "3. <a href=\"/adopt/list\" style=\" font-weight: bolder; font-size: 20px; color: #68a5fe;\">반려동물 입양 정보</a> \n" +
                          "4. <a href=\"/recommend/main\" style=\" font-weight: bolder; font-size: 20px; color: #68a5fe;\">반려동물 추천</a>\n" +
                          "In the form of 'a tags', place the service name and url exactly as I showed you in the HTML tags.\n" +
                          "If a difficult question comes in, or if a user complains, comfort them and provide our company number 111-111-1111 and company email official@elevenliter.com, asking them to contact us. Please answer in Korean and be concise.");
        list.add(0, system);

        return list;
    }

    private Map<String, Object> createRequestBody(List<GptChatVo> list) {
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", "gpt-3.5-turbo");
        reqBody.put("messages", messageSetting(list));
        reqBody.put("temperature", 0.8);
        reqBody.put("max_tokens", 1000);
        return reqBody;
    }


    public Mono<Map> getGptMessage(List<GptChatVo> list) {
        Map<String, Object> requestBody = createRequestBody(list);
        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(Map.class);
    }
}
