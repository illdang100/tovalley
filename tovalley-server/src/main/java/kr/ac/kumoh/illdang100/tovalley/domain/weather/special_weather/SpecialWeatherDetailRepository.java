package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecialWeatherDetailRepository extends JpaRepository<SpecialWeatherDetail, Long> {

    @Query("select sd from SpecialWeatherDetail sd join fetch sd.specialWeather")
    List<SpecialWeatherDetail> findAllWithSpecialWeather();
}
