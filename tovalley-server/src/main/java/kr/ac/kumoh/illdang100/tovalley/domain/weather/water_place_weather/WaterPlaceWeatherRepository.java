package kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaterPlaceWeatherRepository extends JpaRepository<WaterPlaceWeather, Long> {

    List<WaterPlaceWeather> findAllByWaterPlace_Id(Long waterPlaceId);

    void deleteByWaterPlace_Id(Long waterPlaceId);
}
