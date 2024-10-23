package kr.kro.gonggibap.domain.user.controller;

import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // 로그인 성공 시 리다이렉트 페이지
    @Value("${auth.redirect-path}")
    public String REDIRECT_PATH;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        log.info(REDIRECT_PATH);
//        log.info(user.getEmail());
//        log.info(user.getName());
//        log.info(String.valueOf(user.getUserRole()));
        return ResponseEntity.ok("Hello World");
    }
}
