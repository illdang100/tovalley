package kr.ac.kumoh.illdang100.tovalley.service;

public interface MedicalFacilityService {

    // 병원 조회
    HospitalRespDto getHospitalsNearbyWaterPlace(Long waterPlaceId);

    // 약국 조회
    PharmacyRespDto getPharmaciesNearbyWaterPlace(Long waterPlaceId);
}
