package kr.ac.kumoh.illdang100.tovalley.service.page;

import kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageReqDto;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.page.MainPageRespDto.*;

public interface MainPageService {

    MainPageAllRespDto getMainPageAllData();

    AccidentCountDto getTotalAccidents(String province);

    List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond);
}
