package kr.kro.gonggibap.domain.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.kro.gonggibap.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


}
