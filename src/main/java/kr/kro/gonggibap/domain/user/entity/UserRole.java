package kr.kro.gonggibap.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    ADMIN, USER;

    public static boolean isUserRole(String role) {
        for(UserRole u : values()) {
            if(u.name().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}
