package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import kr.ac.kumoh.illdang100.tovalley.form.water_place.CreateWaterPlaceForm;
import kr.ac.kumoh.illdang100.tovalley.form.water_place.WaterPlaceForm;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaterPlaceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "water_place_detail_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id", nullable = false)
    private WaterPlace waterPlace;

    @Column(nullable = false, length = 20)
    private String waterPlaceSegment; // 물놀이구간(m)

    @Column(nullable = false, length = 20)
    private String deepestDepth; // 수심(깊은곳)

    @Column(nullable = false, length = 20)
    private String avgDepth; // 평균 수심

    @Column(length = 20)
    private String annualVisitors; // 연평균 총 이용객수(천명)

    @Column(length = 20)
    private String dangerSegments; // 위험구역구간

    @Column(length = 20)
    private String dangerSignboardsNum; // 위험구역 설정 안내표지판(합계)

    @Column(length = 254)
    private String safetyMeasures; // 안전조치 사항

    @Column(precision = 3, scale = 1)
    private Double waterTemperature; // 계곡 수온(°C)

    @Column(precision = 3, scale = 1)
    private Double bod; // BOD(mg/L)

    @Column(precision = 3, scale = 1)
    private Double turbidity; // 탁도(NTU)

    public void update(WaterPlaceForm form) {
        Double waterPlaceSegment = form.getWaterPlaceSegment();
        Double annualVisitors = form.getAnnualVisitors();
        Integer dangerSegments = form.getDangerSegments();
        Integer dangerSignboardsNum = form.getDangerSignboardsNum();
        String safetyMeasures = form.getSafetyMeasures();

        this.waterPlaceSegment =(waterPlaceSegment == null) ? "-" : String.valueOf(waterPlaceSegment);
        this.deepestDepth = String.valueOf(form.getDeepestDepth());
        this.avgDepth = String.valueOf(form.getAvgDepth());
        this.annualVisitors = annualVisitors == null ? "" : String.valueOf(annualVisitors);
        this.dangerSegments = dangerSegments == null ? "" : String.valueOf(dangerSegments);
        this.dangerSignboardsNum = dangerSignboardsNum == null ? "" : String.valueOf(dangerSignboardsNum);
        this.safetyMeasures = safetyMeasures == null ? "" : safetyMeasures;
        this.waterTemperature = form.getWaterTemperature();
        this.bod = form.getBod();
        this.turbidity = form.getTurbidity();
    }

    public static WaterPlaceDetail createNewWaterPlaceDetail(WaterPlace waterPlace, CreateWaterPlaceForm form) {

        Double waterPlaceSegment = form.getWaterPlaceSegment();
        Double avgDepth = form.getAvgDepth();
        Double annualVisitors = form.getAnnualVisitors();
        Integer dangerSegments = form.getDangerSegments();
        Integer dangerSignboardsNum = form.getDangerSignboardsNum();
        String safetyMeasures = form.getSafetyMeasures();

        return WaterPlaceDetail.builder()
                .waterPlace(waterPlace)
                .waterPlaceSegment((waterPlaceSegment == null) ? "-" : String.valueOf(waterPlaceSegment))
                .deepestDepth(String.valueOf(form.getDeepestDepth()))
                .avgDepth(String.valueOf(avgDepth))
                .annualVisitors(annualVisitors == null ? "" : String.valueOf(annualVisitors))
                .dangerSegments(dangerSegments == null ? "" : String.valueOf(dangerSegments))
                .dangerSignboardsNum(dangerSignboardsNum == null ? "" : String.valueOf(dangerSignboardsNum))
                .safetyMeasures(safetyMeasures == null ? "" : safetyMeasures)
                .waterTemperature(form.getWaterTemperature())
                .bod(form.getBod())
                .turbidity(form.getTurbidity())
                .build();
    }
}
