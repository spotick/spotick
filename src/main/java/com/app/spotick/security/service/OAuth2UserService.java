package com.app.spotick.security.service;

import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.security.type.OAuthType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthClientName = userRequest.getClientRegistration().getClientName();
        OAuthType oAuthType = OAuthType.valueOf(oauthClientName);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("oAuth2User = " + oAuth2User);
        System.out.println("attributes = " + attributes);
        System.out.println("userRequest = " + userRequest);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        }catch ( AuthenticationException e){
            e.printStackTrace();
            throw e;
        } catch (Exception e){
            e.printStackTrace();
        }
        return oAuth2User;
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest,OAuth2User oAuth2User){

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String tokenValue = userRequest.getAccessToken().getTokenValue();

        return null;
    }




}





