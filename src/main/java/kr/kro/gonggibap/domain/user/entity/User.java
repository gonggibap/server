package kr.kro.gonggibap.domain.user.entity;

import jakarta.persistence.*;
import kr.kro.gonggibap.domain.restaurant.entity.FavoriteRestaurant;
import kr.kro.gonggibap.domain.review.entity.Review;
import lombok.*;

import java.util.ArrayList;
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
@ToString(of = {"id", "email","userRole"})
public class User {

    private static final Long POINT = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score", nullable = false)
    private Long score = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<FavoriteRestaurant> favorites = new ArrayList<>();

    public void update(String name) {
        this.name = name;
    }

    public void increaseScore() {
        this.score += POINT;
    }

    public void decreaseScore() {
        if (this.score >= POINT) this.score -= POINT;
    }


    public Long getReviewsCount() {
        return (long) this.reviews.size();
    }

    public Double getReviewsAverage() {
        return this.reviews.stream()
                .mapToDouble(Review::getPoint)
                .average()
                .orElse(0.0);
    }
}
