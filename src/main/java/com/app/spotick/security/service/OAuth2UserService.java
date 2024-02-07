package com.app.spotick.security.service;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.security.type.OAuthType;
import com.app.spotick.service.user.UserProfileFileService;
import com.app.spotick.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository authorityRepository;
    private final UserProfileFileService profileFileService;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
//        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String oauthClientName = userRequest.getClientRegistration().getClientName();
        OAuthType oAuthType = OAuthType.valueOf(oauthClientName.toUpperCase());

        try {
            System.out.println("=========================================");
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
            System.out.println("=========================================");

            return switch (oAuthType) {
                case KAKAO -> createOrFindKakoOAuth2User(oAuth2User.getAttributes(), oAuthType);
            };
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User createOrFindKakoOAuth2User(Map<String, Object> attributes, OAuthType oAuthType) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        String email = (String) kakaoAccount.get("email");
        String profileImgUrl = (String) kakaoProfile.get("profile_image_url");
        boolean isDefaultImg = (boolean) kakaoProfile.get("is_default_image");

        User user = userRepository.findUserAndProfileByEmail(email)
                .orElseGet(() -> {
                    UserProfileFile userProfileFile = null;
                    UserJoinDto kakaoUser = UserJoinDto.fromKakao(kakaoAccount);

                    if (isDefaultImg) {
                        userService.join(kakaoUser);
                        userProfileFile = userRepository.findUserProfileFileByUserId(kakaoUser.getId());
                    } else {
                        userProfileFile = profileFileService.saveImgFromImgUrl(profileImgUrl);
                        userService.join(kakaoUser,userProfileFile);
                    }

                    return kakaoUser.toEntity(userProfileFile);
                });

        return new UserDetailsDto(user, authorityRepository.findUserAuthorityByUser(user), attributes, oAuthType);
    }


}





