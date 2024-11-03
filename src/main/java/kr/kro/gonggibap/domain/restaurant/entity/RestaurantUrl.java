package kr.kro.gonggibap.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "restaurant_urls")
public class RestaurantUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_url_id")
    private Long id;

    @Column(name = "restaurant_original_url", nullable = false, length = 2048)
    private String originalUrl;

    @Column(name = "restaurant_short_url", unique = true, nullable = false)
    private String shortUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "expires_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Builder
    public RestaurantUrl(final String originalUrl, final String shortUrl, final Date createdAt, final Date expiresAt) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
