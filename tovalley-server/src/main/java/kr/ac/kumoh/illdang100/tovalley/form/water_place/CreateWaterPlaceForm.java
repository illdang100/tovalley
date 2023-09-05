package kr.ac.kumoh.illdang100.tovalley.form.water_place;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

/**
 * 물놀이 장소 등록 Form
 */
@Data
public class CreateWaterPlaceForm {

    @NotBlank
    @Size(max = 254, message = "물놀이 장소 명칭은 254자 이하로 작성해주세요")
    private String waterPlaceName; // 물놀이 지역 명칭
    private MultipartFile waterPlaceImage;
    @NotBlank
    @Size(max = 20, message = "시도는 20자 이하로 작성해주세요")
    @Pattern(regexp = "(서울특별시|울산광역시|대전광역시|광주광역시|부산광역시|제주특별자치도|세종특별자치시|강원도|경기도|경상북도|경상남도|전라북도|전라남도|충청북도|충청남도)$")
    private String province; // 시도
    @NotBlank
    @Size(max = 20, message = "시군구는 20자 이하로 작성해주세요")
    private String city; // 시군구
    @Size(max = 20, message = "시도는 20자 이하로 작성해주세요")
    private String town; // 읍면
    @Size(max = 254, message = "세부지명은 254자 이하로 작성해주세요")
    private String subLocation; // 세부지명
    @NotBlank
    @Size(max = 254, message = "주소는 254자 이하로 작성해주세요")
    private String address; // 주소
    @Size(max = 6, message = "구분은 6자 이하로 작성해주세요")
    @Pattern(regexp = "(|계곡|하천|산간계곡|유원지|계곡및소하천|인공호수|하천(강)|저수지)$")
    private String waterPlaceCategory; // 구분(계곡, 하천)
    @NotBlank
    @Size(max = 6, message = "시도는 6자 이하로 작성해주세요")
    @Pattern(regexp = "(일반지역|중점관리지역|위험지역)$")
    private String managementType; // 관리유형(일반지역, 중점관리지역)
    @Min(0)
    private Double waterPlaceSegment; // 물놀이구간(m)
    @NotNull
    @Min(0)
    private Double deepestDepth; // 수심(깊은곳)
    @NotNull
    @Min(0)
    private Double avgDepth; // 평균 수심
    @Min(0)
    private Double annualVisitors; // 연평균 총 이용객수(천명)
    @Min(0)
    private Integer dangerSegments; // 위험구역구간
    @Min(0)
    private Integer dangerSignboardsNum; // 위험구역 설정 안내표지판(합계)
    @Size(max = 254, message = "안전조치 사항은 254자 이하로 작성해주세요")
    private String safetyMeasures; // 안전조치 사항
    private Double waterTemperature; // 계곡 수온(°C)
    @Min(0)
    private Double bod; // BOD(mg/L)
    @Min(0)
    private Double turbidity; // 탁도(NTU)
    @Min(0)
    private Integer lifeBoatNum; // 인명구조함
    @Min(0)
    private Integer portableStandNum; // 이동식거치대
    @Min(0)
    private Integer lifeJacketNum; // 구명조끼
    @Min(0)
    private Integer lifeRingNum; // 구명환
    @Min(0)
    private Integer rescueRopeNum; // 구명로프
    @Min(0)
    private Integer rescueRodNum; // 구조봉
}
