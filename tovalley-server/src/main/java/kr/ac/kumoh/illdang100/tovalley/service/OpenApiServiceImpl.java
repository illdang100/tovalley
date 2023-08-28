package kr.ac.kumoh.illdang100.tovalley.service;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.FileRootPathVO;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegion;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalRegionRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.national_weather.NationalWeatherRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.special_weather.*;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather.WaterPlaceWeather;
import kr.ac.kumoh.illdang100.tovalley.domain.weather.water_place_weather.WaterPlaceWeatherRepository;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.proj4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.nio.charset.StandardCharsets.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenApiServiceImpl implements OpenApiService {

    @Value("${key.openweather}")
    private String openWeatherKey;

    @Value("${key.specialweather}")
    private String specialWeatherKey;

    @Value("${key.waterplace}")
    private String waterPlaceKey;

    @Value("${key.googlemap}")
    private String googleMapKey;

    private static final String PROJECTED_CRS = "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs";
    private static final String WGS84_CRS = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";

    private final WaterPlaceRepository waterPlaceRepository;
    private final WaterPlaceDetailRepository waterPlaceDetailRepository;
    private final RescueSupplyRepository rescueSupplyRepository;

    private final WaterPlaceWeatherRepository waterPlaceWeatherRepository;

    private final SpecialWeatherRepository specialWeatherRepository;
    private final SpecialWeatherDetailRepository specialWeatherDetailRepository;

    private final NationalRegionRepository nationalRegionRepository;
    private final NationalWeatherRepository nationalWeatherRepository;

    private final S3Service s3Service;

    /**
     * @methodnme: fetchAndSaveNationalWeatherData
     * @author: JYeonJun
     * @description: 전국 몇몇 지역의 날씨 정보를 Open API로부터 가져와 데이터베이스에 저장
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 0/30 0 * * *")
    public void fetchAndSaveNationalWeatherData() {

        log.info("전국 지역 날씨 정보 업데이트중!!");

        nationalWeatherRepository.deleteAll();

        List<NationalRegion> nationalRegions = nationalRegionRepository.findAll();

        for (NationalRegion nationalRegion : nationalRegions) {
            JSONObject response = fetchWeatherData(nationalRegion.getCoordinate().getLatitude(), nationalRegion.getCoordinate().getLongitude());
            JSONArray weatherDataList = response.getJSONArray("list");
            List<NationalWeather> waterPlaceWeatherList = extractNationalWeatherData(weatherDataList, nationalRegion);
            nationalWeatherRepository.saveAll(waterPlaceWeatherList);
        }

        log.info("전국 지역 날씨 정보 업데이트 완료!!");
    }

    private List<NationalWeather> extractNationalWeatherData(JSONArray weatherDataList, NationalRegion nationalRegion) {
        List<NationalWeather> nationalWeatherList = new ArrayList<>();

        for (int i = 0; i < weatherDataList.length(); i++) {
            JSONObject weatherData = weatherDataList.getJSONObject(i);
            long dt = weatherData.getLong("dt");
            LocalDate weatherDate = timeConversion(dt);

            double rainPrecipitation = weatherData.optDouble("rain", 0);

            JSONObject temp = weatherData.getJSONObject("temp");
            double min = temp.getDouble("min");
            double max = temp.getDouble("max");

            JSONArray weatherList = weatherData.getJSONArray("weather");
            JSONObject weather = weatherList.getJSONObject(0);
            String climate = weather.getString("main");
            String description = weather.getString("description");
            String climateIcon = weather.getString("icon");

            nationalWeatherList.add(NationalWeather.builder()
                    .nationalRegion(nationalRegion)
                    .climate(climate)
                    .climateIcon(climateIcon)
                    .climateDescription(description)
                    .lowestTemperature(min)
                    .highestTemperature(max)
                    .weatherDate(weatherDate)
                    .rainPrecipitation(rainPrecipitation)
                    .build());
        }
        return nationalWeatherList;
    }

    /**
     * @methodnme: fetchAndSaveSpecialWeatherData
     * @author: JYeonJun
     * @description: 전국 특보 정보를 Open API로부터 가져와 데이터베이스에 저장
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 */30 * * * *")
    public void fetchAndSaveSpecialWeatherData() {

        log.info("특보 정보 업데이트중!!");

        JSONObject specialWeatherData = fetchSpecialWeatherData();

        JSONArray itemArray = getItemArray(specialWeatherData);

        deleteWeatherAlertStatus();

        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject item = itemArray.getJSONObject(i);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            LocalDateTime tmEfTime = LocalDateTime.parse(item.getString("tmEf"), formatter);
            LocalDateTime tmFcTime = LocalDateTime.parse(String.valueOf(item.getLong("tmFc")), formatter);

            extractWeatherAlertStatus(item, tmEfTime, tmFcTime);
            extractPreWeatherAlertStatus(item, tmEfTime, tmFcTime);
        }
        log.info("특보 정보 업데이트 완료!!");
    }

    private JSONArray getItemArray(JSONObject specialWeatherData) {
        return specialWeatherData
                .getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items")
                .getJSONArray("item");
    }

    private void deleteWeatherAlertStatus() {
        specialWeatherDetailRepository.deleteAll();
        specialWeatherRepository.deleteAll();
    }

    private JSONObject fetchSpecialWeatherData() {
        try {

            StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1360000/WthrWrnInfoService/getPwnStatus"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + specialWeatherKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            String result = sb.toString();
            log.debug("special weather open api result = {}", result);
            return new JSONObject(result);
        } catch (IOException e) {
            throw new CustomApiException(e.getMessage());
        }
    }

    private void extractWeatherAlertStatus(JSONObject item, LocalDateTime effectiveTime, LocalDateTime announcementTime) {

        String weatherAlert = item.getString("t6");

        Map<String, String> alertMap = parseAlertString(weatherAlert);

        for (Map.Entry<String, String> entry : alertMap.entrySet()) {

            String title = entry.getKey().substring(2);

            WeatherAlertType weatherAlertType = determineWeatherAlertType(title);

            SpecialWeather savedSpecialWeather = saveSpecialWeather(announcementTime, effectiveTime, weatherAlertType, SpecialWeatherEnum.BREAKING, title);

            saveSpecialWeatherDetail(savedSpecialWeather, entry.getValue());
        }
    }

    private Map<String, String> parseAlertString(String alertString) {
        Map<String, String> alertMap = new HashMap<>();

        String[] lines = alertString.split("\n");

        for (String line : lines) {
            if (line.startsWith("o")) {
                String[] tokens = line.split(":");
                String alertName = tokens[0].trim();
                String alertContent = tokens[1].trim();

                alertMap.put(alertName, alertContent);
            }
        }

        return alertMap;
    }

    private void extractPreWeatherAlertStatus(JSONObject item, LocalDateTime effectiveTime, LocalDateTime announcementTime) {

        String preWeatherAlert = item.getString("t7");

        SpecialWeather specialWeather = null;

        String[] alertSections = preWeatherAlert.split("\\(\\d+\\)\\s+");

        for (String section : alertSections) {
            String[] lines = section.split("\r\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim(); // 현재 줄의 앞뒤 공백 제거

                if (!line.isEmpty()) {
                    if (i == 0) {
                        WeatherAlertType weatherAlertType = determineWeatherAlertType(line);
                        if ("o 없음".equals(line)) return;
                        specialWeather = saveSpecialWeather(announcementTime, effectiveTime, weatherAlertType, SpecialWeatherEnum.PRELIMINARY, line);

                    } else {
                        String content = line.substring(2);
                        saveSpecialWeatherDetail(specialWeather, content);
                    }
                }
            }
        }
    }

    private WeatherAlertType determineWeatherAlertType(String title) {
        if (title.contains("강풍")) {
            return WeatherAlertType.WINDSTORM;
        } else if (title.contains("호우")) {
            return WeatherAlertType.HEAVY_RAIN;
        } else if (title.contains("한파")) {
            return WeatherAlertType.COLD_WAVE;
        } else if (title.contains("건조")) {
            return WeatherAlertType.DROUGHT;
        } else if (title.contains("폭풍해일")) {
            return WeatherAlertType.STORM_SURGE;
        } else if (title.contains("풍랑")) {
            return WeatherAlertType.ROUGH_SEA;
        } else if (title.contains("태풍")) {
            return WeatherAlertType.TYPHOON;
        } else if (title.contains("대설")) {
            return WeatherAlertType.HEAVY_SNOW;
        } else if (title.contains("황사")) {
            return WeatherAlertType.YELLOW_DUST;
        } else if (title.contains("폭염")) {
            return WeatherAlertType.HEAT_WAVE;
        } else {
            return WeatherAlertType.ETC;
        }
    }

    private SpecialWeather saveSpecialWeather(LocalDateTime announcementTime, LocalDateTime effectiveTime, WeatherAlertType weatherAlertType, SpecialWeatherEnum category, String title) {
        return specialWeatherRepository.save(SpecialWeather.builder()
                .announcementTime(announcementTime)
                .effectiveTime(effectiveTime)
                .weatherAlertType(weatherAlertType)
                .category(category)
                .title(title)
                .build());
    }

    private void saveSpecialWeatherDetail(SpecialWeather specialWeather, String content) {
        specialWeatherDetailRepository.save(SpecialWeatherDetail.builder()
                .specialWeather(specialWeather)
                .content(content)
                .build());
    }

    /**
     * @methodnme: fetchAndSaveWaterPlaceWeatherData
     * @author: JYeonJun
     * @description: 물놀이 지역 날씨를 Open API로부터 가져와 데이터베이스에 저장
     */
    @Override
    public List<WaterPlaceWeather> fetchAndSaveWaterPlaceWeatherData(Long waterPlaceId) {
        log.info("물놀이 장소 날씨 정보 업데이트 중!!");

        WaterPlace findWaterPlace = findWaterPlaceByIdOrElseThrowEx(waterPlaceRepository, waterPlaceId);
        JSONArray weatherDataList = fetchWeatherApiResponseArray(findWaterPlace);
        List<WaterPlaceWeather> waterPlaceWeatherList = extractWaterPlaceWeatherData(weatherDataList, findWaterPlace);

        log.info("물놀이 장소 날씨 정보 업데이트 완료!!");
        return waterPlaceWeatherRepository.saveAll(waterPlaceWeatherList);
    }

    private JSONArray fetchWeatherApiResponseArray(WaterPlace findWaterPlace) {
        Coordinate coordinate = findWaterPlace.getCoordinate();
        JSONObject response = fetchWeatherData(coordinate.getLatitude(), coordinate.getLongitude());
        return response.getJSONArray("list");
    }

    private List<WaterPlaceWeather> extractWaterPlaceWeatherData(JSONArray weatherDataList, WaterPlace waterPlace) {
        List<WaterPlaceWeather> waterPlaceWeatherList = new ArrayList<>();

        for (int i = 0; i < weatherDataList.length(); i++) {
            JSONObject weatherData = weatherDataList.getJSONObject(i);
            long dt = weatherData.getLong("dt");
            LocalDate weatherDate = timeConversion(dt);

            double rainPrecipitation = weatherData.optDouble("rain", 0);
            double windSpeed = weatherData.optDouble("speed", 0);
            int clouds = weatherData.optInt("clouds", 0);
            int humidity = weatherData.optInt("humidity", 0);

            JSONObject temp = weatherData.getJSONObject("temp");
            double min = temp.getDouble("min");
            double max = temp.getDouble("max");

            JSONArray weatherList = weatherData.getJSONArray("weather");
            JSONObject weather = weatherList.getJSONObject(0);
            String climate = weather.getString("main");
            String description = weather.getString("description");
            String climateIcon = weather.getString("icon");

            JSONObject feelsLike = weatherData.getJSONObject("feels_like");
            double dayFeelsLike = feelsLike.getDouble("day");

            waterPlaceWeatherList.add(WaterPlaceWeather.builder()
                    .waterPlace(waterPlace)
                    .climate(climate)
                    .climateIcon(climateIcon)
                    .climateDescription(description)
                    .lowestTemperature(min)
                    .highestTemperature(max)
                    .weatherDate(weatherDate)
                    .humidity(humidity)
                    .windSpeed(windSpeed)
                    .rainPrecipitation(rainPrecipitation)
                    .clouds(clouds)
                    .dayFeelsLike(dayFeelsLike)
                    .build());
        }
        return waterPlaceWeatherList;
    }

    private JSONObject fetchWeatherData(String lat, String lon) {

        try {
            StringBuilder urlBuilder = new StringBuilder("https://pro.openweathermap.org/data/2.5/forecast/climate"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("appid", "UTF-8") + "=" + openWeatherKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8")); /*위도*/
            urlBuilder.append("&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8")); /*경도*/
            urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=" + URLEncoder.encode("metric", "UTF-8")); /*섭씨온도*/
            urlBuilder.append("&" + URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode("kr", "UTF-8")); /*한국어*/
            urlBuilder.append("&" + URLEncoder.encode("cnt", "UTF-8") + "=" + URLEncoder.encode("5", "UTF-8")); /*한국어*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            return new JSONObject(sb.toString());
        } catch (IOException e) {
            throw new CustomApiException(e.getMessage());
        }
    }

    private LocalDate timeConversion(long utcTimestamp) {

        // UTC 시간을 LocalDateTime으로 변환
        Instant instant = Instant.ofEpochSecond(utcTimestamp);
        LocalDateTime utcDateTime = instant.atZone(ZoneOffset.UTC).toLocalDateTime();

        // 서울 시간으로 변환
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        LocalDate seoulDate = utcDateTime.atZone(seoulZoneId).toLocalDate();

        return seoulDate;
    }

    /**
     * @methodnme: fetchAndSaveWaterPlacesData
     * @author: JYeonJun
     * @description: 행정안전부에서 제공해주는 전국 물놀이 관리지역 정보 가져와 데이터 저장
     */
    @Override
    @Transactional
    public void fetchAndSaveWaterPlacesData() {

        log.debug("행정안전부 물놀이관리지역 open api 시작!!");

        JSONObject waterPlacesData = fetchWaterPlacesData();

        JSONArray items = waterPlacesData.getJSONObject("response")
                .getJSONObject("body")
                .getJSONArray("items");

        List<WaterPlace> waterPlaceList = new ArrayList<>();
        List<WaterPlaceDetail> waterPlaceDetailList = new ArrayList<>();
        List<RescueSupply> rescueSupplyList = new ArrayList<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            String waterPlaceName = item.getString("WTRPLAY_PLC_NM");
            String wpName = waterPlaceName.replaceAll("\\s", "");

            if (!isWaterPlaceExist(waterPlaceName)) {
                ImageFile waterPlaceImage = saveWaterPlaceImage(wpName);
                WaterPlace waterPlace = createWaterPlace(item, waterPlaceImage);

                WaterPlaceDetail waterPlaceDetail = createWaterPlaceDetail(item, waterPlace);
                RescueSupply rescueSupply = createRescueSupply(item, waterPlace);

                waterPlaceList.add(waterPlace);
                waterPlaceDetailList.add(waterPlaceDetail);
                rescueSupplyList.add(rescueSupply);
            }
        }

        saveWaterPlaceData(waterPlaceList, waterPlaceDetailList, rescueSupplyList);

        log.debug("행정안전부 물놀이관리지역 open api 종료!!");
    }

    private boolean isWaterPlaceExist(String waterPlaceName) {
        return waterPlaceRepository.existsByWaterPlaceName(waterPlaceName);
    }

    private void saveWaterPlaceData(List<WaterPlace> waterPlaceList, List<WaterPlaceDetail> waterPlaceDetailList,
                                    List<RescueSupply> rescueSupplyList) {

        waterPlaceRepository.saveAll(waterPlaceList);
        waterPlaceDetailRepository.saveAll(waterPlaceDetailList);
        rescueSupplyRepository.saveAll(rescueSupplyList);
    }

    private WaterPlace createWaterPlace(JSONObject item, ImageFile waterPlaceImage) {
        double x = item.getDouble("X");
        double y = item.getDouble("Y");
        double[] latLng = convertToLatLon(x, y);
        Coordinate coordinate = new Coordinate(String.valueOf(latLng[0]), String.valueOf(latLng[1]));

        String province = item.getString("CTPRVN_NM");
        if ("충정북도".equals(province)) {
            province = "충청북도";
        }

        return WaterPlace.builder()
                .waterPlaceName(item.getString("WTRPLAY_PLC_NM"))
                .province(province)
                .city(item.getString("SGG_NM"))
                .town(item.optString("EMD_NM", ""))
                .subLocation(item.optString("DETAIL_PLCNM", ""))
                .address(item.getString("ADRES"))
                .waterPlaceCategory(item.optString("WTRPLAY_PLC_TYPE", ""))
                .coordinate(coordinate)
                .managementType(item.getString("MANAGEMENT_TYPE"))
                .rating(0.0)
                .waterPlaceImage(waterPlaceImage)
                .build();
    }

    private WaterPlaceDetail createWaterPlaceDetail(JSONObject item, WaterPlace waterPlace) {

        double waterTemperature = generateRandomValue(20.0, 30.0);
        double bod = generateRandomValue(0.0, 6.0);
        double turbidity = generateRandomValue(1.0, 50.0);

        return WaterPlaceDetail.builder()
                .waterPlace(waterPlace)
                .waterPlaceSegment(item.getString("WTRPLAY_SEC"))
                .deepestDepth(item.getString("WTRPLAY_DEEP"))
                .avgDepth(item.getString("WTRPLAY_DEEP_AVG"))
                .annualVisitors(item.optString("YEAR_VISITOR", ""))
                .dangerSegments(item.optString("WTRPLAY_ER", ""))
                .dangerSignboardsNum(item.optString("ER_SIGN_CO", ""))
                .safetyMeasures(item.optString("SAFETY_ACT", ""))
                .waterTemperature(roundToOneDecimalPlace(waterTemperature))
                .bod(roundToOneDecimalPlace(bod))
                .turbidity(roundToOneDecimalPlace(turbidity))
                .build();
    }

    private double generateRandomValue(double minValue, double maxValue) {
        Random random = new Random();
        return minValue + (maxValue - minValue) * random.nextDouble();
    }

    private double roundToOneDecimalPlace(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private RescueSupply createRescueSupply(JSONObject item, WaterPlace waterPlace) {
        return RescueSupply.builder()
                .waterPlace(waterPlace)
                .lifeBoatNum(getIntValueFromItem(item, "HUMNLF_RESCUSHIP_CNT"))
                .portableStandNum(getIntValueFromItem(item, "ROVNGNS_STANDS_CNT"))
                .lifeJacketNum(getIntValueFromItem(item, "RESCUE_VEST_CNT"))
                .lifeRingNum(getIntValueFromItem(item, "LIFEBUOY_CNT"))
                .rescueRopeNum(getIntValueFromItem(item, "RESCUE_ROPE_CNT"))
                .rescueRodNum(getIntValueFromItem(item, "RESCUBNG_CNT"))
                .build();
    }

    private int getIntValueFromItem(JSONObject item, String key) {
        return item.optInt(key, -1);
    }

    private JSONObject fetchWaterPlacesData() {

        try {
            StringBuilder strBuilder = new StringBuilder("https://www.safemap.go.kr/openApiService/data/getWtrPlayData.do"); /*URL*/
            strBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(waterPlaceKey, "UTF-8")); /*Service Key*/
            strBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            strBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1365", "UTF-8")); /*한 페이지 결과 수*/
            strBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값), JSON*/
            URL url = new URL(strBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            return new JSONObject(sb.toString());
        } catch (IOException e) {
            throw new CustomApiException(e.getMessage());
        }
    }

    private double[] convertToLatLon(double x, double y) {
        CoordinateTransform transform = createTransform(PROJECTED_CRS, WGS84_CRS);
        ProjCoordinate projectedCoord = new ProjCoordinate(x, y);
        ProjCoordinate wgs84Coord = new ProjCoordinate();
        transform.transform(projectedCoord, wgs84Coord);

        double latitude = wgs84Coord.y;
        double longitude = wgs84Coord.x;
        return new double[]{latitude, longitude};
    }

    private CoordinateTransform createTransform(String sourceCrs, String targetCrs) {
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem sourceCRS = crsFactory.createFromParameters("sourceCRS", sourceCrs);
        CoordinateReferenceSystem targetCRS = crsFactory.createFromParameters("targetCRS", targetCrs);
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        return ctFactory.createTransform(sourceCRS, targetCRS);
    }

    /**
     * 물놀이 장소 사진 저장
     * @param waterPlaceName
     * @return
     */
    private ImageFile saveWaterPlaceImage(String waterPlaceName) {
        try {
            String placeId = findPlaceId(waterPlaceName);
            if (placeId == null || placeId.trim().isEmpty())
                return null;

            List<String> photoAttributions = findPhotoAttributions(placeId);
            if (photoAttributions == null || photoAttributions.isEmpty())
                return null;

            ImageFile placeImageFile = findPlaceImage(photoAttributions, FileRootPathVO.WATER_PLACE_PATH, waterPlaceName);
            log.debug("waterPlace={} url={}", waterPlaceName, placeImageFile.getStoreFileUrl());

            return placeImageFile;
        } catch (IOException e) {
            throw new CustomApiException("사진 저장을 실패했습니다.\n" + e.getMessage());
        }
    }

    private ImageFile findPlaceImage(List<String> photoAttributions, String fileRootPath, String waterPlaceName) throws IOException {
        String apiUrl = "https://maps.googleapis.com/maps/api/place/photo";
        Map<String, String> params = new HashMap<>();
        params.put("photo_reference", photoAttributions.get(1));
        params.put("maxwidth", photoAttributions.get(0));

        String googleApiUrl = buildGoogleApiUrl(apiUrl, params);
        URL url = new URL(googleApiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        InputStream inputStream = con.getInputStream();
        byte[] imageBytes = inputStream.readAllBytes();

        String filaName = "_" + waterPlaceName + ".jpg";

        MultipartFile multipartFile = new MockMultipartFile(filaName, filaName, "image/jpeg", imageBytes);
        ImageFile uploadFile = s3Service.upload(multipartFile, fileRootPath);

        return uploadFile;
    }

    private List<String> findPhotoAttributions(String placeId) throws IOException {
        String apiUrl = "https://maps.googleapis.com/maps/api/place/details/json";
        String field = "photos";
        Map<String, String> params = new HashMap<>();
        params.put("place_id", placeId);
        params.put("fields", field);
        String googleApiUrl = buildGoogleApiUrl(apiUrl, params);


        JSONObject jsonResponse = getJsonObject(googleApiUrl);
        JSONObject result = jsonResponse.getJSONObject("result");
        if (result.isEmpty()) return null;
        JSONArray photos = result.getJSONArray("photos");
        JSONObject jsonObject = photos.getJSONObject(0);
        String width = jsonObject.get("width").toString();
        String photoReference = jsonObject.get("photo_reference").toString();

        return Arrays.asList(width, photoReference);
    }

    private String findPlaceId(String waterPlaceName) throws IOException {
        String apiUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json";
        String inputType = "textquery";
        Map<String, String> params = new HashMap<>();
        params.put("input", waterPlaceName);
        params.put("inputtype", inputType);
        String googleApiUrl = buildGoogleApiUrl(apiUrl, params);

        JSONObject jsonResponse = getJsonObject(googleApiUrl);
        JSONArray candidates = jsonResponse.getJSONArray("candidates");
        if (candidates.isEmpty()) return null;
        JSONObject jsonObject = candidates.getJSONObject(0);

        return jsonObject.get("place_id").toString();
    }

    private String buildGoogleApiUrl(String baseUrl, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append("?key=" + googleMapKey);
        sb.append("&");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        sb.deleteCharAt(sb.length() - 1); // Remove the last '&' character

        return sb.toString();
    }

    private static JSONObject getJsonObject(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }

        reader.close();
        con.disconnect();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse;
    }
}
