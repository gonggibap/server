package kr.kro.gonggibap.core.config.oauth;

import kr.kro.gonggibap.domain.user.dto.CustomOAuth2User;
import kr.kro.gonggibap.domain.user.dto.KakaoResponse;
import kr.kro.gonggibap.domain.user.dto.OAuth2Response;
import kr.kro.gonggibap.domain.user.dto.UserDto;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2User 객체를 가져온다.
        // 요청을 바탕으로 유저 정보를 담은 객체 반환
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2User: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId: {}", registrationId);

        // 각각의 리소스 서버가 제공해주는 데이터가 다르기 때문에 전처리 과정이 필요함.
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String email = oAuth2Response.getEmail();
        System.out.println("email : " + email);
        Optional<User> existData = userRepository.findByEmail(email);

        // 첫 로그인
        if (existData.isEmpty()) {
            User newUser = oAuth2Response.toEntity();

            userRepository.save(newUser);

            UserDto userDto = UserDto.builder()
                    .email(oAuth2Response.getEmail())
                    .name(newUser.getName())
                    .build();

            log.info("new user: {}", userDto);

            System.out.println("새로운 로그인 : " + userDto);
            return new CustomOAuth2User(userDto);
        }
        // 이미 로그인 한 회원일 경우(DB에 저장된 유저)
        else {
            // 회원 email은 안바뀔 것이므로 따로 설정 x

            User existingUser = existData.get();

            log.info("existing user: {}", existingUser);

            userRepository.save(existingUser);

            UserDto userDto = UserDto.builder()
                    .email(oAuth2Response.getEmail())
                    .name(existingUser.getName())
                    .build();

            log.info("Updated user: {}", userDto.getEmail());
            return new CustomOAuth2User(userDto);
        }
    }

}

