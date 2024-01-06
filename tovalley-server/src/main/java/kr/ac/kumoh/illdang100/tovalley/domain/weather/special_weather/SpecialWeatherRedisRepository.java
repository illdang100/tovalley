package kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather;

import org.springframework.data.repository.CrudRepository;

public interface SpecialWeatherRedisRepository extends CrudRepository<SpecialWeather, String> {
}