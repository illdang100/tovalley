package kr.ac.kumoh.illdang100.tovalley.domain.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WaterQualityReviewEnum {

    CLEAN("깨끗해요"), FINE("괜찮아요"), DIRTY("더러워요");
    private String value;
}