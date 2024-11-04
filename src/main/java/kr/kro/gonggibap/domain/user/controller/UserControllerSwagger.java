package kr.kro.gonggibap.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.domain.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Tag(name = "회원 관리", description = "회원 관리 API")
public interface UserControllerSwagger {

    @Operation(
            summary = "회원 조회", description = "로그인한 사용자가 자신의 정보를 조회.",
            parameters = {
                    @Parameter(name = AUTHORIZATION, description = "access token", in = ParameterIn.HEADER, required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/")
    ResponseEntity<?> getUserInfo(@Parameter(hidden = true) @LoginUser User user);

}

