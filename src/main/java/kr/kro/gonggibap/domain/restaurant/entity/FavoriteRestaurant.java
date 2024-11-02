package kr.kro.gonggibap.domain.restaurant.entity;

import jakarta.persistence.*;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorite_restaurants")
@ToString(of = {"id"})
public class FavoriteRestaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_restaurant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Builder
    public FavoriteRestaurant(final User user, final Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }
}
