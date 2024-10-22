package kr.kro.gonggibap.domain.publicoffice.controller;

import kr.kro.gonggibap.domain.publicoffice.service.PublicOfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public-offices")
public class PublicOfficeController {

    private final PublicOfficeService publicOfficeService;
}
