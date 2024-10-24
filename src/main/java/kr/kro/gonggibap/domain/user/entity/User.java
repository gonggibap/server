package kr.kro.gonggibap.domain.user.entity;

import jakarta.persistence.*;
import kr.kro.gonggibap.domain.review.entity.Review;
import lombok.*;

import java.util.List;

@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_unique", columnNames = "email")
        })
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    public void update(String name) {
        this.name = name;
    }
}
