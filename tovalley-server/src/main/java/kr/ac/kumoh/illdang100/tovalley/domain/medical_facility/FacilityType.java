package kr.ac.kumoh.illdang100.tovalley.domain.medical_facility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FacilityType {

    HOSPITAL("병원"), PHARMACY("약국");
    private String value;
}
