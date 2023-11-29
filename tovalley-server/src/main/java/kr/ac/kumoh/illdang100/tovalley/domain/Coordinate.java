package kr.ac.kumoh.illdang100.tovalley.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coordinate {

    @Column(nullable = false, length = 20)
    private String latitude;

    @Column(nullable = false, length = 20)
    private String longitude;
}
