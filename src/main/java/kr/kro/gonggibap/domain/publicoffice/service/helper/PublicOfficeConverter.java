package kr.kro.gonggibap.domain.publicoffice.service.helper;

import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeResponse;
import kr.kro.gonggibap.domain.publicoffice.entity.PublicOffice;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PublicOfficeConverter {
    public static PublicOfficeResponse getPublicOfficeResponse(PublicOffice publicOffice) {
        return new PublicOfficeResponse(publicOffice.getId(),
                publicOffice.getName(),
                publicOffice.getAddress(),
                publicOffice.getRoadAddress(),
                publicOffice.getLatitude(),
                publicOffice.getLongitude()
        );
    }

    public static List<PublicOfficeResponse> getPublicOfficeResponses(List<PublicOffice> publicOffices) {
        return publicOffices.stream()
                .map(publicOffice -> new PublicOfficeResponse(publicOffice.getId(),
                publicOffice.getName(),
                publicOffice.getAddress(),
                publicOffice.getRoadAddress(),
                publicOffice.getLatitude(),
                publicOffice.getLongitude()
        )).toList();
    }
}
