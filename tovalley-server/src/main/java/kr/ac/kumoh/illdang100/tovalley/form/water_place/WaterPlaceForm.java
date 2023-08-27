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

    @NotNull
    private Long id; // waterPlace PK

    @NotBlank
    @Size(max = 254, message = "물놀이 장소 명칭은 254자 이하로 작성해주세요")
    private String waterPlaceName; // 물놀이 지역 명칭

    @Size(max = 250, message = "물놀이 장소 사진은 250자 이하로 작성해주세요")
    private String waterPlaceImageUrl;

    @NotBlank
    @Size(max = 20, message = "시도는 20자 이하로 작성해주세요")
    private String province; // 시도

    @NotBlank
    @Size(max = 20, message = "시군구는 20자 이하로 작성해주세요")
    private String city; // 시군구

    @NotBlank
    @Size(max = 20, message = "시도는 20자 이하로 작성해주세요")
    private String town; // 읍면

    @NotBlank
    @Size(max = 254, message = "세부지명은 254자 이하로 작성해주세요")
    private String subLocation; // 세부지명

    @NotBlank
    @Size(max = 254, message = "주소는 254자 이하로 작성해주세요")
    private String address; // 주소

    @NotBlank
    @Size(max = 20, message = "구분은 20자 이하로 작성해주세요")
    private String waterPlaceCategory; // 구분(계곡, 하천)

    @NotBlank
    @Size(max = 20, message = "시도는 20자 이하로 작성해주세요")
    private String managementType; // 관리유형(일반지역, 중점관리지역)

    @NotBlank
    @Size(max = 20, message = "물놀이구간은 20자 이하로 작성해주세요")
    private String waterPlaceSegment; // 물놀이구간(m)

    @NotBlank
    @Size(max = 20, message = "수심(깊은곳)은 20자 이하로 작성해주세요")
    private String deepestDepth; // 수심(깊은곳)

    @NotBlank
    @Size(max = 20, message = "평균 수심은 20자 이하로 작성해주세요")
    private String avgDepth; // 평균 수심

    @Size(max = 20, message = "연평균 총 이용객수는 20자 이하로 작성해주세요")
    private String annualVisitors; // 연평균 총 이용객수(천명)

    @Size(max = 20, message = "위험구역구간은 20자 이하로 작성해주세요")
    private String dangerSegments; // 위험구역구간

    @Size(max = 20, message = "위험구역 설정 안내표지판(합계)은 20자 이하로 작성해주세요")
    private String dangerSignboardsNum; // 위험구역 설정 안내표지판(합계)

    @Size(max = 254, message = "안전조치 사항은 254자 이하로 작성해주세요")
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
