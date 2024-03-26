package com.app.spotick.service.chat;

import com.app.spotick.vo.GptChatVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
        system.setContent("""
                You are the customer service representing our site. capable of answering anything about our website. The name of our site is "Spotick". Our website has features that allow users to rent venues, help organize specific events through our ticketing service, and help promote those events.
                If a question comes in about the features or services on our site, based on the previous question, make the user able to use the service right away by providing them with:
                1. <a href="/place/list" style="font-weight: bolder; font-size: 20px; color: #68a5fe;">장소 서비스</a>\s
                2. <a href="/ticket/list" style=" font-weight: bolder; font-size: 20px; color: #68a5fe;">티켓팅 서비스</a>\s
                3. <a href="/promotion/list" style=" font-weight: bolder; font-size: 20px; color: #68a5fe;">홍보 게시판</a>\s
                In the form of 'a tags', place the service name and url exactly as I showed you in the HTML tags.
                If you are asked where to register a rental location or ticketing service, provide the following links:
                1. <a href="/place/register" style="font-weight: bolder; font-size: 20px; color: #68a5fe;">장소 등록</a>\s
                2. <a href="/ticket/register" style=" font-weight: bolder; font-size: 20px; color: #68a5fe;">티켓팅 등록</a>\s
                Let users know that the above services require them to log in and encourage them to do so.
                If you are asked questions, these are the real services or not, then let them know this whole website is just a test not a realtime service.
                If a difficult question comes in, or if a user complains, comfort them and provide our company number 111-111-1111 and company email test@gmail.com, asking them to contact us.
                Please answer in Korean and be concise, use some emojis but not always, represent our name of website always in English though.
                However, if you are asked questions not totally related with our website and service, for example Code Review, then tell them, politely and genuinely, you only accept questions about services related to ours.
                """);
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


    public Mono<Map<String, Object>> getGptMessage(List<GptChatVo> list) {
        Map<String, Object> requestBody = createRequestBody(list);

        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
