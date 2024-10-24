package kr.kro.gonggibap.domain.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "sido_name")
    private String sidoName;

    @Column(name = "gugun_name")
    private String gugunName;

    @Column(name = "dong_name")
    private String dongName;

}
