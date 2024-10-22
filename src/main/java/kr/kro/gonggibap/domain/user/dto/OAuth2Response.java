package kr.kro.gonggibap.domain.user.dto;

import kr.kro.gonggibap.domain.user.entity.User;

public interface OAuth2Response {
    //제공자 (Ex. naver, google, ...)
    String getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    //이메일
    String getEmail();

    public User toEntity();
}
