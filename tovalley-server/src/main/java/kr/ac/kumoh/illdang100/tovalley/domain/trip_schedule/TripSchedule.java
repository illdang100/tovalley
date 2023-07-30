package kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule;

import kr.ac.kumoh.illdang100.tovalley.domain.valley.Valley;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TripSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "valley_id")
    private Valley valley;

    @Column(nullable = false)
    private LocalDate tripDate;

    @Column(nullable = false)
    private int tripNumber;
}
