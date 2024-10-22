package kr.kro.gonggibap.domain.publicoffice.service;

import kr.kro.gonggibap.domain.publicoffice.repository.PublicOfficeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PublicOfficeService {

    private final PublicOfficeRepository publicOfficeRepository;
}
