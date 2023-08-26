package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import lombok.Data;

/**
 * 물놀이 장소 등록 Form
 */
@Data
public class CreateWaterPlaceForm {

    // TODO: validation 어노테이션 추가

    private String waterPlaceName; // 물놀이 지역 명칭
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
}
