package kr.ac.kumoh.illdang100.tovalley.service.domain.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
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

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaterPlaceServiceImpl implements WaterPlaceService {

    private final WaterPlaceRepository waterPlaceRepository;

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

        return new NationalPopularWaterPlacesDto(wp.getId(), wp.getWaterPlaceName(), location, formattedRating, reviewCount);
    }

    private String getFormattedRating(Double rating) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(rating);
    }

}
