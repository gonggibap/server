package kr.kro.gonggibap.domain.publicoffice.service;

import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeResponse;
import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeRestaurantResponse;
import kr.kro.gonggibap.domain.publicoffice.entity.PublicOffice;
import kr.kro.gonggibap.domain.publicoffice.repository.PublicOfficeRepository;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.NOT_FOUND_PUBLIC_OFFICE;
import static kr.kro.gonggibap.domain.publicoffice.service.helper.PublicOfficeConverter.getPublicOfficeResponse;
import static kr.kro.gonggibap.domain.publicoffice.service.helper.PublicOfficeConverter.getPublicOfficeResponses;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PublicOfficeService {

    private final PublicOfficeRepository publicOfficeRepository;

    public PublicOfficeRestaurantResponse getPublicOffice(Long id) {
        PublicOffice publicOffice = publicOfficeRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PUBLIC_OFFICE));

        List<RestaurantResponse> restaurantResponses = publicOfficeRepository.findRestaurantsByPublicOfficeId(id);

        return getPublicOfficeResponse(publicOffice, restaurantResponses);
    }


    public List<PublicOfficeResponse> getPublicOffices() {
        List<PublicOffice> publicOffices = publicOfficeRepository.findAll();

        return getPublicOfficeResponses(publicOffices);
    }

}
