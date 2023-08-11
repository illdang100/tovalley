package kr.ac.kumoh.illdang100.tovalley.dto.page;

import kr.ac.kumoh.illdang100.tovalley.domain.ProvinceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class MainPageReqDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveYearlyAccidentCondition {

        @NotNull
        private ProvinceEnum province;
    }

    @AllArgsConstructor
    @Getter
    public static class RetrievePopularWaterPlacesCondition {

        @NotNull
        @Pattern(regexp = "(RATING|REVIEW)$")
        private String cond;
    }
}
