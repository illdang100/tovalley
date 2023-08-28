package kr.ac.kumoh.illdang100.tovalley.form.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.water_place.RescueSupply;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceDetail;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 물놀이 장소 조회 Form
 */
@Data
public class WaterPlaceForm {

    private Long id; // waterPlace PK
    private String waterPlaceName; // 물놀이 지역 명칭
    private String waterPlaceImage;
    private String province; // 시도
    private String city; // 시군구
    private String town; // 읍면
    private String subLocation; // 세부지명
    private String address; // 주소
    private String waterPlaceCategory; // 구분(계곡, 하천)
    private String managementType; // 관리유형(일반지역, 중점관리지역)
    private String waterPlaceSegment; // 물놀이구간(m)
    private String deepestDepth; // 수심(깊은곳)
    private String avgDepth; // 평균 수심
    private String annualVisitors; // 연평균 총 이용객수(천명)
    private String dangerSegments; // 위험구역구간
    private String dangerSignboardsNum; // 위험구역 설정 안내표지판(합계)
    private String safetyMeasures; // 안전조치 사항
    private Double waterTemperature; // 계곡 수온(°C)
    private Double bod; // BOD(mg/L)
    private Double turbidity; // 탁도(NTU)
    private Integer lifeBoatNum; // 인명구조함
    private Integer portableStandNum; // 이동식거치대
    private Integer lifeJacketNum; // 구명조끼
    private Integer lifeRingNum; // 구명환
    private Integer rescueRopeNum; // 구명로프
    private Integer rescueRodNum; // 구조봉

    public WaterPlaceForm(WaterPlace waterPlace, WaterPlaceDetail waterPlaceDetail, RescueSupply rescueSupply) {
        this.id = waterPlace.getId();
        this.waterPlaceName = waterPlace.getWaterPlaceName();
        this.province = waterPlace.getProvince();
        this.city = waterPlace.getCity();
        this.town = waterPlace.getTown();
        this.subLocation = waterPlace.getSubLocation();
        this.address = waterPlace.getAddress();
        this.waterPlaceCategory = waterPlace.getWaterPlaceCategory();
        this.managementType = waterPlace.getManagementType();
        this.waterPlaceSegment = waterPlaceDetail.getWaterPlaceSegment();
        this.deepestDepth = waterPlaceDetail.getDeepestDepth();
        this.avgDepth = waterPlaceDetail.getAvgDepth();
        this.annualVisitors = waterPlaceDetail.getAnnualVisitors();
        this.dangerSegments = waterPlaceDetail.getDangerSegments();
        this.dangerSignboardsNum = waterPlaceDetail.getDangerSignboardsNum();
        this.safetyMeasures = waterPlaceDetail.getSafetyMeasures();
        this.waterTemperature = waterPlaceDetail.getWaterTemperature();
        this.bod = waterPlaceDetail.getBod();
        this.turbidity = waterPlaceDetail.getTurbidity();
        this.lifeBoatNum = rescueSupply.getLifeBoatNum();
        this.portableStandNum = rescueSupply.getPortableStandNum();
        this.lifeJacketNum = rescueSupply.getLifeJacketNum();
        this.lifeRingNum = rescueSupply.getLifeRingNum();
        this.rescueRopeNum = rescueSupply.getRescueRopeNum();
        this.rescueRodNum = rescueSupply.getRescueRodNum();
    }
}
