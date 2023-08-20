package kr.ac.kumoh.illdang100.tovalley.dummy;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewImage;
import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.RescueSupply;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceDetail;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegion;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeatherDetail;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.SpecialWeatherEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.WeatherAlertType;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather.WaterPlaceWeather;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DummyObject {

    protected Member newMember(String username, String nickname) {

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encPassword = passwordEncoder.encode("1234");

        return Member.builder()
                .username(username)
                .memberName("name")
                .password("임시 비밀번호")
                .email(username + "@naver.com")
                .nickname(nickname)
                .imageFile(null)
                .role(MemberEnum.CUSTOMER)
                .build();
    }

    protected Member newMockMember(Long id, String username, String nickname, MemberEnum memberEnum) {

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encPassword = passwordEncoder.encode("1234");
        return Member.builder()
                .id(id)
                .username(username)
                .memberName("name")
                .password("임시 비밀번호")
                .email(username + "@naver.com")
                .nickname(nickname)
                .role(memberEnum)
                .build();
    }

    protected WaterPlace newWaterPlace(String name, String province, Double rating, Integer reviewCnt) {

        return WaterPlace.builder()
                .waterPlaceName(name)
                .province(province)
                .city("city")
                .town("town")
                .subLocation("subLocation")
                .address("address")
                .waterPlaceCategory("계곡")
                .coordinate(new Coordinate("38.10000000", "128.10000000"))
                .managementType("일반지역")
                .rating(rating)
                .reviewCount(reviewCnt)
                .build();
    }

    protected WaterPlace newWaterPlaceWithCity(String name, String province, String city, Double rating, Integer reviewCnt) {

        return WaterPlace.builder()
                .waterPlaceName(name)
                .province(province)
                .city(city)
                .town("town")
                .subLocation("subLocation")
                .address("address")
                .waterPlaceCategory("계곡")
                .coordinate(new Coordinate("38.10000000", "128.10000000"))
                .managementType("일반지역")
                .rating(rating)
                .reviewCount(reviewCnt)
                .build();
    }

    protected WaterPlace newMockWaterPlace(Long id, String name, String province, Double rating, Integer reviewCnt) {

        return WaterPlace.builder()
                .id(id)
                .waterPlaceName(name)
                .province(province)
                .city("city")
                .town("town")
                .subLocation("subLocation")
                .address("address")
                .waterPlaceCategory("계곡")
                .coordinate(new Coordinate("37.46822799867968", "126.94553962920112"))
                .managementType("일반지역")
                .rating(rating)
                .reviewCount(reviewCnt)
                .build();
    }

    protected Accident newAccident(WaterPlace waterPlace, LocalDate accidentDate,
                                       AccidentEnum accidentCondition, Integer peopleNum) {

        return Accident.builder()
                .waterPlace(waterPlace)
                .accidentDate(accidentDate)
                .accidentCondition(accidentCondition)
                .peopleNum(peopleNum)
                .build();
    }

    protected Accident newMockAccident(Long id, WaterPlace waterPlace, LocalDate accidentDate,
                                       AccidentEnum accidentCondition, Integer peopleNum) {

        return Accident.builder()
                .id(id)
                .waterPlace(waterPlace)
                .accidentDate(accidentDate)
                .accidentCondition(accidentCondition)
                .peopleNum(peopleNum)
                .build();
    }

    protected TripSchedule newTripSchedule(Member member, WaterPlace waterPlace,
                                           LocalDate tripDate, int tripNum) {

        return TripSchedule.builder()
                .member(member)
                .waterPlace(waterPlace)
                .tripDate(tripDate)
                .tripNumber(tripNum)
                .build();
    }

    protected Review newReview(WaterPlace waterPlace, Member member,
                               String content, Integer rating,
                               WaterQualityReviewEnum waterQualityReviewEnum,
                               WaterPlaceRepository waterPlaceRepository) {

        waterPlace.calculateRating(rating);
        waterPlaceRepository.save(waterPlace);

        return Review.builder()
                .waterPlace(waterPlace)
                .member(member)
                .reviewContent(content)
                .rating(rating)
                .waterQualityReview(waterQualityReviewEnum)
                .build();
    }

    protected ReviewImage newReviewImage(Review review, String storeUrl) {

        ImageFile imageFile = new ImageFile("storeFileName", storeUrl);

        return ReviewImage.builder()
                .review(review)
                .imageFile(imageFile)
                .build();
    }

    protected Review newMockReview(Long id, WaterPlace waterPlace, Member member,
                               String content, Integer rating) {

        return Review.builder()
                .id(id)
                .waterPlace(waterPlace)
                .member(member)
                .reviewContent(content)
                .rating(rating)
                .build();
    }

    protected NationalRegion newNationalRegion(String regionName) {

        return NationalRegion.builder()
                .regionName(regionName)
                .coordinate(new Coordinate("11.11111111", "111.11111111"))
                .build();
    }

    protected NationalRegion newMockNationalRegion(Long id, String regionName) {

        return NationalRegion.builder()
                .id(id)
                .regionName(regionName)
                .coordinate(new Coordinate("11.11111111", "111.11111111"))
                .build();
    }

    protected NationalWeather newNationalWeather(NationalRegion nationalRegion, String climate,
                                                     LocalDate weatherDate) {

        return NationalWeather.builder()
                .nationalRegion(nationalRegion)
                .climate(climate)
                .climateIcon("10d")
                .climateDescription("굵은 비")
                .lowestTemperature(24.7)
                .highestTemperature(35.9)
                .weatherDate(weatherDate)
                .rainPrecipitation(0.78)
                .build();
    }

    protected NationalWeather newMockNationalWeather(Long id, NationalRegion nationalRegion, String climate,
                                                     LocalDate weatherDate) {

        return NationalWeather.builder()
                .id(id)
                .nationalRegion(nationalRegion)
                .climate(climate)
                .climateIcon("10d")
                .climateDescription("굵은 비")
                .lowestTemperature(24.7)
                .highestTemperature(35.9)
                .weatherDate(weatherDate)
                .rainPrecipitation(0.78)
                .build();
    }

    protected SpecialWeather newSpecialWeather (LocalDateTime time, WeatherAlertType weatherAlertType,
                                                SpecialWeatherEnum category, String title) {

        return SpecialWeather.builder()
                .announcementTime(time)
                .effectiveTime(time)
                .weatherAlertType(weatherAlertType)
                .category(category)
                .title(title)
                .build();
    }

    protected SpecialWeatherDetail newSpecialWeatherDetail (SpecialWeather specialWeather, String content) {

        return SpecialWeatherDetail.builder()
                .specialWeather(specialWeather)
                .content(content)
                .build();

    }

    protected WaterPlaceWeather newWaterPlaceWeather (WaterPlace waterPlace, LocalDate date) {

        return WaterPlaceWeather.builder()
                .waterPlace(waterPlace)
                .climate("Rain")
                .climateIcon("10d")
                .climateDescription("굵은 비")
                .lowestTemperature(11.1)
                .highestTemperature(28.9)
                .weatherDate(date)
                .humidity(18)
                .windSpeed(0.48)
                .rainPrecipitation(11.1)
                .clouds(10)
                .dayFeelsLike(25.5)
                .build();
    }

    protected WaterPlaceDetail newWaterPlaceDetail (WaterPlace waterPlace) {

        return WaterPlaceDetail.builder()
                .waterPlace(waterPlace)
                .waterPlaceSegment("10")
                .deepestDepth("5")
                .avgDepth("1")
                .annualVisitors("2.5")
                .dangerSegments("1")
                .dangerSignboardsNum("10")
                .safetyMeasures("안전조치사항")
                .waterTemperature(25.8)
                .bod(4.5)
                .turbidity(2.3)
                .build();
    }

    protected RescueSupply newRescueSupply (WaterPlace waterPlace) {

        return RescueSupply.builder()
                .waterPlace(waterPlace)
                .lifeBoatNum(10)
                .portableStandNum(10)
                .lifeJacketNum(10)
                .lifeRingNum(10)
                .rescueRopeNum(10)
                .rescueRodNum(10)
                .build();
    }
}
