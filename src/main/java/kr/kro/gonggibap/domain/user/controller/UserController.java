package kr.kro.gonggibap.domain.user.controller;

import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.user.dto.UserDto;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerSwagger{
    private final UserService userService;

    /**
     * 사용자의 정보를 반환하는 API
     * 로그인 된 사용자가 자신의 정보를 조회할 때 사용함.
     * @param user
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getUserInfo(@LoginUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_EXISTS);
        }
        UserDto userDto = UserDto.of(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInfo(@LoginUser User user, @PathVariable Long id) {
        if (user == null || user.getId() != id) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZATION);
        }

        UserDto userDto = userService.findById(id);

        return ResponseEntity.ok(userDto);
    }
}
