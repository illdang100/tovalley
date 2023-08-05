package kr.ac.kumoh.illdang100.tovalley.domain.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.waterplace.WaterPlace;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accident_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id")
    private WaterPlace waterPlace;

    @Column(nullable = false)
    private LocalDate accidentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 13)
    private AccidentEnum accidentCondition;

    @Column(nullable = false)
    private Integer peopleNum;
}
