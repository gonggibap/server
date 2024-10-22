package kr.kro.gonggibap.domain.user.dto;

import java.time.LocalDateTime;

public record TokenDetails(String token, LocalDateTime expireTime) {
}
