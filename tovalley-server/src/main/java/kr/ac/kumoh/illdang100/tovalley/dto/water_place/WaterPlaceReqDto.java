package kr.ac.kumoh.illdang100.tovalley.dto.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.CityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.util.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class WaterPlaceReqDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveWaterPlacesCondition {
        @NotNull
        @Pattern(regexp = "(전국|서울특별시|인천광역시|세종특별자치시|대전광역시|광주광역시|대구광역시|부산광역시|울산광역시|경기도|강원도|충청북도|충청남도|전라북도|전라남도|경상북도|경상남도|제주특별자치도)")
        private String province;

        @Nullable
        private CityEnum city;

        @Nullable
        private String searchWord;

        @NotNull
        private String sortCond;

        @NotNull
        private int page;
    }

    @AllArgsConstructor
    @Data
    public static class AdminRetrieveWaterPlacesCondition {
        private String searchWord;
    }
}
