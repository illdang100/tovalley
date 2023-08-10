package kr.ac.kumoh.illdang100.tovalley.web.page;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto;
import kr.ac.kumoh.illdang100.tovalley.service.domain.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TestApiController {

    private final WeatherService weatherService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {

        List<MainPageRespDto.NationalWeatherRespDto> nationalWeathers = weatherService.getNationalWeathers();
        return new ResponseEntity<>(new ResponseDto<>(1, "메인 페이지 조회에 성공하였습니다", nationalWeathers), HttpStatus.OK);
    }
}
