package kr.kro.gonggibap.domain.user.dto;

import kr.kro.gonggibap.core.util.RandomNicknameGenerator;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.entity.UserRole;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccount;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;

        System.out.println("??들어오나");
        System.out.println(attributes);

        log.debug("attribute: {}", attributes.get("kakao_account"));
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.get("email").toString();
    }

    public String makeNickname() {
        String email = getEmail();
        return hashString(email);
    }

    private String hashString(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString().substring(0, 10);
    }

    @Override
    public User toEntity() {

        String email = this.getEmail();

        return User.builder()
                .email(email)
                .name(RandomNicknameGenerator.generateRandomNickname())
                .userRole(UserRole.USER)
                .build();
    }
}
