package kr.kro.gonggibap.domain.image.entity;

import jakarta.persistence.*;
import kr.kro.gonggibap.domain.review.entity.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "images")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    private String imageUrl;

    public Image(Review review, String imageUrl) {
        this.review = review;
        this.imageUrl = imageUrl;
    }
}
