package kr.kro.gonggibap.domain.publicoffice.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class PublicOfficeResponse {
    private final Long id;
    private final String name;
    private final String address;
    private final String roadAddress;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
}
