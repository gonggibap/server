package kr.kro.gonggibap.domain.restaurant.entity;

import jakarta.persistence.*;
import kr.kro.gonggibap.domain.history.entity.History;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "restaurants")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String restaurantName;

    @Column(length = 255)
    private String link;

    @Column(length = 255)
    private String category;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 255)
    private String roadAddress;

    @Column(precision = 10, scale = 7, nullable = false)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7, nullable = false)
    private BigDecimal longitude;

    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point location;

    private Boolean needCheck;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE)
    private List<History> histories;
}