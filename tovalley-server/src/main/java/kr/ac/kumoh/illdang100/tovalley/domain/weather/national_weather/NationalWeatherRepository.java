package kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NationalWeatherRepository extends JpaRepository<NationalWeather, Long> {

    @Query("select nw from NationalWeather nw join fetch nw.nationalRegion")
    List<NationalWeather> findAllWithNationalRegion();
}
