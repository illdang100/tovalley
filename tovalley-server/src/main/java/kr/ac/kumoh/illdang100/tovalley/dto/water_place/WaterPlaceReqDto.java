package kr.ac.kumoh.illdang100.tovalley.dto.water_place;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.util.annotation.Nullable;
import javax.validation.constraints.NotNull;

public class WaterPlaceReqDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveWaterPlacesCondition {
        @NotNull
        private String region;

        @Nullable
        private String searchWord;
    }
}
