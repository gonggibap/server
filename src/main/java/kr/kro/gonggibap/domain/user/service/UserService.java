package kr.kro.gonggibap.domain.user.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.BusinessException;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow( () -> new BusinessException(ErrorCode.USER_NOT_EXISTS));
    }
}
