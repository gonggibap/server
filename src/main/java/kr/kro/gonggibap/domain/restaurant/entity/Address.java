package kr.kro.gonggibap.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @Column(name = "address_code")
    private String code;

    @Column(name = "sido_name")
    private String sidoName;

    @Column(name = "gugun_name")
    private String gugunName;

    @Column(name = "dong_name")
    private String dongName;

    @OneToMany(mappedBy = "address", cascade = CascadeType.REMOVE)
    private List<Restaurant> restaurants;
}
