package kr.ac.kumoh.illdang100.tovalley.service.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaterPlaceServiceImpl implements WaterPlaceService {

    private final WaterPlaceRepository waterPlaceRepository;
    private final WaterPlaceDetailRepository waterPlaceDetailRepository;
    private final RescueSupplyRepository rescueSupplyRepository;

    /**
     * // 물놀이 장소 리스트 조회 페이지
     * @param retrieveWaterPlacesCondition
     * @param pageable
     * @return
     */
    @Override
    public Page<RetrieveWaterPlacesDto> getWaterPlaces(RetrieveWaterPlacesCondition retrieveWaterPlacesCondition, Pageable pageable) {
        return null;
    }

    /**
     * @methodnme: getPopularWaterPlaces
     * @author: JYeonJun
     * @description: 전국 인기 계곡 리스트(8개) 조회 메서드
     *
     * @return: 인기 계곡 내림차순(평점 or 리뷰수) 정보
     */
    @Override
    public List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond) {
        List<WaterPlace> waterPlaces;

        Pageable pageable = PageRequest.of(0, 8);

        if ("RATING".equals(cond)) {
            waterPlaces = waterPlaceRepository.findTop8ByOrderByRatingDesc(pageable);
        } else {
            waterPlaces = waterPlaceRepository.findTop8ByOrderByReviewCountDesc(pageable);
        }

        return waterPlaces.stream()
                .map(this::mapToPopularWaterPlaceDto)
                .collect(Collectors.toList());
    }

    private NationalPopularWaterPlacesDto mapToPopularWaterPlaceDto(WaterPlace wp) {
        String location = wp.getProvince() + " " + wp.getCity();
        int reviewCount = wp.getReviewCount();
        Double rating = wp.getRating();
        String formattedRating = getFormattedRating(rating);
        String waterPlaceImageUrl = wp.getWaterPlaceImageUrl();

        return new NationalPopularWaterPlacesDto(wp.getId(), wp.getWaterPlaceName(), location, formattedRating, reviewCount, waterPlaceImageUrl);
    }

    private String getFormattedRating(Double rating) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(rating);
    }

    /**
     * @methodnme: getWaterPlaceDetailByWaterPlace
     * @author: JYeonJun
     * @param waterPlaceId: 물놀이 장소 pk
     * @description: 물놀이 장소 상세정보 조회
     * @return: 물놀이 장소 상세정보
     */
    @Override
    public WaterPlaceDetailRespDto getWaterPlaceDetailByWaterPlace(Long waterPlaceId) {
        WaterPlaceDetail findWaterPlaceDetail = findWaterPlaceDetailByWaterPlaceIdOrElseThrowEx(waterPlaceId);

        WaterPlace findWaterPlace = findWaterPlaceDetail.getWaterPlace();
        Coordinate coordinate = findWaterPlace.getCoordinate();

        return createWaterPlaceDetailRespDto(findWaterPlace, coordinate, findWaterPlaceDetail);
    }

    private WaterPlaceDetail findWaterPlaceDetailByWaterPlaceIdOrElseThrowEx(Long waterPlaceId) {
        return waterPlaceDetailRepository.findWaterPlaceDetailWithWaterPlaceById(waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]가 존재하지 않습니다"));
    }

    private WaterPlaceDetailRespDto createWaterPlaceDetailRespDto(WaterPlace waterPlace, Coordinate coordinate, WaterPlaceDetail waterPlaceDetail) {
        return WaterPlaceDetailRespDto.builder()
                .waterPlaceImage(waterPlace.getWaterPlaceImageUrl())
                .waterPlaceName(waterPlace.getWaterPlaceName())
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .detailAddress(waterPlace.getAddress() + " " + waterPlace.getSubLocation())
                .town(waterPlace.getTown())
                .annualVisitors(waterPlaceDetail.getAnnualVisitors())
                .safetyMeasures(waterPlaceDetail.getSafetyMeasures())
                .waterPlaceSegment(waterPlaceDetail.getWaterPlaceSegment())
                .dangerSegments(waterPlaceDetail.getDangerSegments())
                .dangerSignboardsNum(waterPlaceDetail.getDangerSignboardsNum())
                .waterTemperature(waterPlaceDetail.getWaterTemperature())
                .bod(waterPlaceDetail.getBod())
                .turbidity(waterPlaceDetail.getTurbidity())
                .build();
    }

    /**
     * @methodnme: getRescueSuppliesByWaterPlace
     * @author: JYeonJun
     * @param waterPlaceId: 물놀이 장소 pk
     * @description: 물놀이 장소에 배치된 구조용품 현황 조회
     * @return: 구조용품 수량
     */
    @Override
    public RescueSupplyByWaterPlaceRespDto getRescueSuppliesByWaterPlace(Long waterPlaceId) {

        RescueSupply findRescueSupply = findRescueSupplyByWaterPlaceIdOrElseThrowEx(waterPlaceId);

        return createRescueSupplyRespDto(findRescueSupply);
    }

    private RescueSupplyByWaterPlaceRespDto createRescueSupplyRespDto(RescueSupply findRescueSupply) {
        return RescueSupplyByWaterPlaceRespDto.builder()
                .lifeBoatNum(findRescueSupply.getLifeBoatNum())
                .portableStandNum(findRescueSupply.getPortableStandNum())
                .lifeJacketNum(findRescueSupply.getLifeJacketNum())
                .lifeRingNum(findRescueSupply.getLifeRingNum())
                .rescueRopeNum(findRescueSupply.getRescueRopeNum())
                .rescueRodNum(findRescueSupply.getRescueRodNum())
                .build();
    }

    private RescueSupply findRescueSupplyByWaterPlaceIdOrElseThrowEx(Long waterPlaceId) {
        return rescueSupplyRepository.findByWaterPlace_Id(waterPlaceId)
        .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]: 구급용품이 존재하지 않습니다"));
    }
}
