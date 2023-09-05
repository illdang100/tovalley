package kr.ac.kumoh.illdang100.tovalley.domain.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.form.water_place.CreateWaterPlaceForm;
import kr.ac.kumoh.illdang100.tovalley.form.water_place.WaterPlaceEditForm;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaterPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "water_place_id")
    private Long id;

    @Column(nullable = false, length = 254)
    private String waterPlaceName; // 물놀이 지역 명칭

    @Column(nullable = false, length = 20)
    private String province; // 시도

    @Column(nullable = false, length = 20)
    private String city; // 시군구

    @Column(nullable = false, length = 20)
    private String town; // 읍면

    @Column(nullable = false, length = 254)
    private String subLocation; // 세부지명

    @Column(nullable = false, length = 254)
    private String address; // 주소

    @Column(nullable = false, length = 20)
    private String waterPlaceCategory; // 구분(계곡, 하천 등)

    @Embedded
    private Coordinate coordinate; // 위경도 좌표

    @Column(nullable = false, length = 20)
    private String managementType; // 관리유형(일반지역, 중점관리지역, 위험지역)

    @Column(nullable = false)
    private Double rating; // 평점

    @Column(nullable = false)
    private int reviewCount; // 리뷰 개수

    @Column(length = 250)
    private ImageFile waterPlaceImage;

    public void calculateRating(Integer rating) {
        double sum = this.rating * reviewCount + rating;
        this.reviewCount++;
        this.rating = sum / reviewCount;
    }

    public void update(WaterPlaceEditForm form) {
        this.waterPlaceName = form.getWaterPlaceName();
        this.province = form.getProvince();
        this.city = form.getCity();
        this.town = form.getTown();
        this.subLocation = form.getSubLocation();
        this.address = form.getAddress();
        this.waterPlaceCategory = form.getWaterPlaceCategory();
        this.managementType = form.getManagementType();
    }

    public static WaterPlace createNewWaterPlace(CreateWaterPlaceForm form, Coordinate coordinate) {

        String town = form.getTown();
        String subLocation = form.getSubLocation();

        return WaterPlace.builder()
                .waterPlaceName(form.getWaterPlaceName())
                .province(form.getProvince())
                .city(form.getCity())
                .town(town == null ? "" : town)
                .subLocation(subLocation == null ? "" : subLocation)
                .address(form.getAddress())
                .waterPlaceCategory(form.getWaterPlaceCategory())
                .coordinate(coordinate)
                .managementType(form.getManagementType())
                .rating(0.0)
                .reviewCount(0)
                .waterPlaceImage(null)
                .build();
    }

    public void changeWaterPlaceImage(ImageFile waterPlaceImage) {
        this.waterPlaceImage = waterPlaceImage;
    }
}
