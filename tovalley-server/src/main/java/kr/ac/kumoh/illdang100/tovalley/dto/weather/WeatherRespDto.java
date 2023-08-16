package kr.ac.kumoh.illdang100.tovalley.dto.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class WeatherRespDto {

    @AllArgsConstructor
    @Data
    public static class SpecialWeatherRespDto {

        private String type;
        private String title;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime announcementDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime effectiveDate;
        private String content;
    }
}
