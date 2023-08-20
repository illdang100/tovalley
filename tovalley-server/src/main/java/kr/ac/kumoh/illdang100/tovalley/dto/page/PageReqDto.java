package kr.ac.kumoh.illdang100.tovalley.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.ac.kumoh.illdang100.tovalley.domain.ProvinceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PageReqDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveYearlyAccidentCondition {

        @NotNull
        @Schema(description = "행정구역" , example = "NATIONWIDE")
        private ProvinceEnum province;
    }

    @AllArgsConstructor
    @Getter
    public static class RetrievePopularWaterPlacesCondition {

        @NotNull
        @Pattern(regexp = "(RATING|REVIEW)$")
        @Schema(description = "인기 물놀이 장소 검색 조건" , example = "REVIEW")
        private String cond;
    }
}
