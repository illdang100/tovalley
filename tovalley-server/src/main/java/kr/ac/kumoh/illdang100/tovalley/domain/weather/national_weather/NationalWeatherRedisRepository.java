package kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather;

import org.springframework.data.repository.CrudRepository;

public interface NationalWeatherRedisRepository extends CrudRepository<NationalWeather, String> {
}
