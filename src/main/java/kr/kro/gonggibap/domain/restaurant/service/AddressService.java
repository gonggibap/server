package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.domain.restaurant.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
    private final AddressRepository addressRepository;

    public boolean isExistDongCode(String dongCode) {
        return addressRepository.existsById(dongCode);
    }
}
