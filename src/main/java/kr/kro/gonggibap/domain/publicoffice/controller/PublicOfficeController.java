package kr.kro.gonggibap.domain.publicoffice.controller;

import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeResponse;
import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeRestaurantResponse;
import kr.kro.gonggibap.domain.publicoffice.service.PublicOfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public-offices")
public class PublicOfficeController implements PublicOfficeControllerSwagger{

    private final PublicOfficeService publicOfficeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPublicOffice(@PathVariable Long id) {
        PublicOfficeRestaurantResponse publicOffice = publicOfficeService.getPublicOffice(id);

        return ResponseEntity.ok(success(publicOffice));
    }

    @GetMapping("")
    public ResponseEntity<?> getPublicOffices() {
        List<PublicOfficeResponse> publicOffices = publicOfficeService.getPublicOffices();

        return ResponseEntity.ok(success(publicOffices));
    }
}
