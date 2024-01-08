package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash("PopularWaterPlaces")
public class PopularWaterPlaces {

    @Id
    private String id;

    private Long waterPlaceId; // 물놀이 지역 pk
    private String waterPlaceName; // 물놀이 지역 이름
    private String location; // province + city
    private String rating; // 평점
    private Integer reviewCnt; // 리뷰 개수
    private String waterPlaceImageUrl; // 물놀이 지역 이미지
    private String condition; // "RATING" or "REVIEW"

    public PopularWaterPlaces(Long waterPlaceId, String waterPlaceName, String location, String rating,
                              Integer reviewCnt, String waterPlaceImageUrl, String condition) {
        this.waterPlaceId = waterPlaceId;
        this.waterPlaceName = waterPlaceName;
        this.location = location;
        this.rating = rating;
        this.reviewCnt = reviewCnt;
        this.waterPlaceImageUrl = waterPlaceImageUrl;
        this.condition = condition;
    }
}
