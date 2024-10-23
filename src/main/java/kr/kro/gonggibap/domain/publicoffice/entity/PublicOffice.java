package kr.kro.gonggibap.domain.publicoffice.entity;

import jakarta.persistence.*;
import kr.kro.gonggibap.domain.history.entity.History;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "public_offices")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_office_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

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

    @OneToMany(mappedBy = "publicOffice", cascade = CascadeType.REMOVE)
    private List<History> histories;
}
