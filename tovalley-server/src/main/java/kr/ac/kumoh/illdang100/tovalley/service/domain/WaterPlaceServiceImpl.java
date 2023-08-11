package kr.ac.kumoh.illdang100.tovalley.service.domain;

import kr.ac.kumoh.illdang100.tovalley.domain.waterplace.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.waterplace.WaterPlaceRepository;
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
    public Page<RetrieveWaterPlaceDto> getWaterPlaces(RetrieveWaterPlacesCondition retrieveWaterPlacesCondition, Pageable pageable) {
        return null;
    }

    @Override
    public List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond) {
        List<WaterPlace> waterPlaces;

        if ("RATING".equals(cond)) {
            waterPlaces = waterPlaceRepository.findTop4ByOrderByRatingDesc(PageRequest.of(0, 4));
        } else {
            waterPlaces = waterPlaceRepository.findTop4ByOrderByReviewCountDesc(PageRequest.of(0, 4));
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
